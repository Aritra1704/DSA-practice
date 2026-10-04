package com.aritra.adyen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Timeout(value = 20, unit = TimeUnit.SECONDS)
class IdempotentExecutorTest {

    // ---- 1. sequential ----

    @Test
    void firstCallRunsActionAndReturnsResult() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        assertThat(ex.execute("k", () -> "OK-1")).isEqualTo("OK-1");
    }

    @Test
    void repeatedSequentialCallsRunActionOnce() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        AtomicInteger runs = new AtomicInteger();
        Callable<String> action = () -> "OK-" + runs.incrementAndGet();

        assertThat(ex.execute("pay-1", action)).isEqualTo("OK-1");
        assertThat(ex.execute("pay-1", action)).isEqualTo("OK-1");
        assertThat(ex.execute("pay-1", action)).isEqualTo("OK-1");
        assertThat(runs.get()).isEqualTo(1);
    }

    @Test
    void differentKeysEachRunTheirOwnAction() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        assertThat(ex.execute("a", () -> "A")).isEqualTo("A");
        assertThat(ex.execute("b", () -> "B")).isEqualTo("B");
        assertThat(ex.execute("a", () -> "other")).isEqualTo("A");
    }

    @Test
    void nullResultIsAlsoRememberedAndActionNotRerun() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        AtomicInteger runs = new AtomicInteger();
        Callable<String> action = () -> {
            runs.incrementAndGet();
            return null;
        };
        assertThat(ex.execute("n", action)).isNull();
        assertThat(ex.execute("n", action)).isNull();
        assertThat(runs.get()).isEqualTo(1);
    }

    // ---- 2. same-key race ----

    @Test
    void sameKeyRaceRunsActionExactlyOnceAndSharesResult() throws Exception {
        int threads = 50;
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        AtomicInteger runs = new AtomicInteger();
        CountDownLatch startGun = new CountDownLatch(1);
        Callable<String> slow = () -> {
            Thread.sleep(300);
            return "OK-" + runs.incrementAndGet();
        };

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                futures.add(pool.submit(() -> {
                    startGun.await();
                    return ex.execute("pay-2", slow);
                }));
            }
            startGun.countDown();
            Set<String> results = new HashSet<>();
            for (Future<String> f : futures) {
                results.add(f.get(10, TimeUnit.SECONDS));
            }
            assertThat(runs.get()).isEqualTo(1);
            assertThat(results).containsExactly("OK-1");
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void sameKeyWaitersBlockUntilFirstExecutionFinishes() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger runs = new AtomicInteger();

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<String> first = pool.submit(() -> ex.execute("k", () -> {
                runs.incrementAndGet();
                started.countDown();
                release.await();
                return "done";
            }));
            assertThat(started.await(5, TimeUnit.SECONDS)).isTrue();

            Future<String> second = pool.submit(() -> ex.execute("k", () -> {
                runs.incrementAndGet();
                return "second-should-not-run";
            }));

            // waiter must still be blocked while the first action is running
            Thread.sleep(300);
            assertThat(second.isDone()).as("waiter returned before first action finished").isFalse();

            release.countDown();
            assertThat(first.get(5, TimeUnit.SECONDS)).isEqualTo("done");
            assertThat(second.get(5, TimeUnit.SECONDS)).isEqualTo("done");
            assertThat(runs.get()).isEqualTo(1);
        } finally {
            pool.shutdownNow();
        }
    }

    // ---- 3. different-key parallelism ----

    @Test
    void differentKeyDoesNotWaitForSlowKey() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        CountDownLatch slowStarted = new CountDownLatch(1);
        CountDownLatch releaseSlow = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<String> slow = pool.submit(() -> ex.execute("slow", () -> {
                slowStarted.countDown();
                releaseSlow.await();
                return "SLOW";
            }));
            assertThat(slowStarted.await(5, TimeUnit.SECONDS)).isTrue();

            Future<String> fast = pool.submit(() -> ex.execute("fast", () -> "FAST"));
            // must finish while "slow" is still held open
            assertThat(fast.get(2, TimeUnit.SECONDS)).isEqualTo("FAST");
            assertThat(slow.isDone()).isFalse();

            releaseSlow.countDown();
            assertThat(slow.get(5, TimeUnit.SECONDS)).isEqualTo("SLOW");
        } finally {
            releaseSlow.countDown();
            pool.shutdownNow();
        }
    }

    @Test
    void manyDifferentKeysRunInParallel() throws Exception {
        int keys = 8;
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        // every action waits for all others to arrive: only passes if they truly run concurrently
        CountDownLatch allArrived = new CountDownLatch(keys);

        ExecutorService pool = Executors.newFixedThreadPool(keys);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < keys; i++) {
                String key = "k" + i;
                futures.add(pool.submit(() -> ex.execute(key, () -> {
                    allArrived.countDown();
                    if (!allArrived.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("actions were serialized, not parallel");
                    }
                    return key;
                })));
            }
            for (int i = 0; i < keys; i++) {
                assertThat(futures.get(i).get(10, TimeUnit.SECONDS)).isEqualTo("k" + i);
            }
        } finally {
            pool.shutdownNow();
        }
    }

    // ---- 4. failure propagation ----

    @Test
    void failureReachesCaller() {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        assertThatThrownBy(() -> ex.execute("f", () -> {
            throw new IOException("declined");
        })).isInstanceOf(IOException.class).hasMessage("declined");
    }

    @Test
    void failureReachesAllConcurrentWaitersAndActionRunsOnce() throws Exception {
        int threads = 20;
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        AtomicInteger runs = new AtomicInteger();
        CountDownLatch startGun = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                futures.add(pool.submit(() -> {
                    startGun.await();
                    return ex.execute("pay-5", () -> {
                        runs.incrementAndGet();
                        Thread.sleep(200);
                        throw new IOException("declined");
                    });
                }));
            }
            startGun.countDown();
            for (Future<String> f : futures) {
                assertThatThrownBy(() -> f.get(10, TimeUnit.SECONDS))
                        .isInstanceOf(ExecutionException.class)
                        .hasCauseInstanceOf(IOException.class)
                        .hasRootCauseMessage("declined");
            }
            assertThat(runs.get()).as("action runs once even when it fails").isEqualTo(1);
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void failedKeyStaysFailedOnLaterSequentialCall() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        AtomicInteger runs = new AtomicInteger();

        assertThatThrownBy(() -> ex.execute("f", () -> {
            runs.incrementAndGet();
            throw new IOException("declined");
        })).isInstanceOf(IOException.class);

        // requirement 4: the key stays failed; a later call must not re-run and must not succeed
        assertThatThrownBy(() -> ex.execute("f", () -> {
            runs.incrementAndGet();
            return "SHOULD-NOT-RUN";
        })).isInstanceOf(IOException.class).hasMessage("declined");
        assertThat(runs.get()).isEqualTo(1);
    }

    @Test
    void errorThrownByActionIsAlsoRecordedAndNotRerun() {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        AtomicInteger runs = new AtomicInteger();

        assertThatThrownBy(() -> ex.execute("err", () -> {
            runs.incrementAndGet();
            throw new AssertionError("boom");
        })).isInstanceOf(AssertionError.class);

        // an Error is still a failed execution: the key stays failed, the action must not run again
        assertThatThrownBy(() -> ex.execute("err", () -> {
            runs.incrementAndGet();
            return "SHOULD-NOT-RUN";
        })).isInstanceOf(AssertionError.class).hasMessage("boom");
        assertThat(runs.get()).isEqualTo(1);
    }

    // ---- 5. key identity ----

    @Test
    void equalButDistinctStringObjectsShareOneExecution() throws Exception {
        int threads = 50;
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        AtomicInteger runs = new AtomicInteger();
        CountDownLatch startGun = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                // runtime-built key: equal value, different String object per caller
                String key = new String("pay-" + "identity");
                futures.add(pool.submit(() -> {
                    startGun.await();
                    return ex.execute(key, () -> {
                        Thread.sleep(200);
                        return "OK-" + runs.incrementAndGet();
                    });
                }));
            }
            startGun.countDown();
            Set<String> results = new HashSet<>();
            for (Future<String> f : futures) {
                results.add(f.get(10, TimeUnit.SECONDS));
            }
            assertThat(runs.get()).isEqualTo(1);
            assertThat(results).containsExactly("OK-1");
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void failureOnOneKeyDoesNotAffectOtherKeys() throws Exception {
        IdempotentExecutor<String> ex = new IdempotentExecutor<>();
        assertThatThrownBy(() -> ex.execute("bad", () -> {
            throw new IOException("x");
        })).isInstanceOf(IOException.class);
        assertThat(ex.execute("good", () -> "fine")).isEqualTo("fine");
    }
}
