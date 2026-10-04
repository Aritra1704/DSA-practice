package com.aritra.adyen;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Round 1: Idempotent payment executor.
 *
 * Requirements (as stated by the interviewer):
 *  1. execute(key, action): if `key` has never been seen, run `action` and return its result.
 *  2. If the same key is submitted again (sequentially OR concurrently), `action` must NOT
 *     run a second time. Every caller gets the same result as the first execution.
 *  3. Concurrent callers with the same key block until the first execution finishes.
 *     Callers with different keys must not block each other.
 *  4. If `action` throws, every waiting caller sees that failure. The key stays failed
 *     for now (retry policy is a follow-up discussion).
 *  5. Thread-safe. No global lock around the whole method.
 *
 * Write it in Java, narrate your design as you go. Target: 45 minutes.
 */
public class IdempotentExecutor<R> {

    /** Result of the first execution: either a value (possibly null) or the exception it threw. */
    private record Outcome<R>(R value, Throwable error) {}

    private final ConcurrentHashMap<String, Outcome<R>> map = new ConcurrentHashMap<>();

    // One lock object per key value, so equal keys always share a monitor and different keys never do.
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    public R execute(String idempotencyKey, Callable<R> action) throws Exception {
        Object lock = locks.computeIfAbsent(idempotencyKey, k -> new Object());
        synchronized (lock) {
            Outcome<R> outcome = map.get(idempotencyKey);
            if (outcome == null) {
                try {
                    outcome = new Outcome<>(action.call(), null);
                } catch (Throwable e) {
                    outcome = new Outcome<>(null, e);
                }
                map.put(idempotencyKey, outcome);
            }
            if (outcome.error() != null) {
                if (outcome.error() instanceof Exception) {
                    throw (Exception) outcome.error();
                } else {
                    throw (Error) outcome.error();
                }
            }
            return outcome.value();
        }
    }

//    final ConcurrentHashMap<String, CompletableFuture<R>> map = new ConcurrentHashMap<>();
//
//    public R execute(String idempotencyKey, Callable<R> action) throws Exception {
//        synchronized (idempotencyKey) {
//            CompletableFuture<R> result = map.get(idempotencyKey);
//            if (result != null) {
//                result.
//                return result.get();
//            }
//
//            try {
//                map.put(idempotencyKey, CompletableFuture.completedFuture(action.call()));
//                return map.get(idempotencyKey).get();
//            } catch (Exception e) {
//                CompletableFuture ex = CompletableFuture.failedFuture(e);
//                map.put(idempotencyKey, ex);
//                throw e;
//            }
//        }
//    }

//    public R execute(String idempotencyKey, Callable<R> action) throws Exception {
//        R result = map.get(idempotencyKey);
//        if (result != null) {
//            return result;
//        } else {
//            synchronized (map) {
//                result = map.get(idempotencyKey);
//                if (result != null) {
//                    return result;
//                }
//                R actionResult = action.call();
//                map.put(idempotencyKey, actionResult);
//                return actionResult;
//            }
//        }
//    }
}
