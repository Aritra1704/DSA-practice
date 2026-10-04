package com.aritra.leetcode.arrays;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class P015_3SumTest {

    private final P015_3Sum solution = new P015_3Sum();

    @Test
    void example1_standard() {
        // [-1,0,1,2,-1,-4] → [[-1,-1,2],[-1,0,1]]
        assertThat(solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4}))
                .containsExactlyInAnyOrder(
                        List.of(-1, -1, 2),
                        List.of(-1, 0, 1)
                );
    }

    @Test
    void example2_zerosOnly() {
        // [0,1,1] → []
        assertThat(solution.threeSum(new int[]{0, 1, 1})).isEmpty();
    }

    @Test
    void example3_allZeros() {
        // [0,0,0] → [[0,0,0]]
        assertThat(solution.threeSum(new int[]{0, 0, 0}))
                .containsExactly(List.of(0, 0, 0));
    }

    @Test
    void edgeCase_noDuplicateTriplets() {
        // multiple duplicate numbers → deduplicated result
        assertThat(solution.threeSum(new int[]{-2, 0, 0, 2, 2}))
                .containsExactly(List.of(-2, 0, 2));
    }

    @Test
    void edgeCase_allNegative() {
        // no triplet can sum to 0
        assertThat(solution.threeSum(new int[]{-3, -2, -1})).isEmpty();
    }

    @Test
    void edgeCase_allPositive() {
        // no triplet can sum to 0
        assertThat(solution.threeSum(new int[]{1, 2, 3})).isEmpty();
    }

    @Test
    void edgeCase_multipleTriplets() {
        assertThat(solution.threeSum(new int[]{-4, -1, -1, 0, 1, 2}))
                .containsExactlyInAnyOrder(
                        List.of(-1, -1, 2),
                        List.of(-1, 0, 1)
                );
    }
}
