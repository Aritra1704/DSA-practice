package com.aritra.leetcode.arrays;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class P053_MaximumSubarrayTest {

    private final P053_MaximumSubarray solution = new P053_MaximumSubarray();

    @Test
    void example1_standard() {
        // [4,-1,2,1] = 6
        assertThat(solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}))
                .isEqualTo(6);
    }

    @Test
    void example2_singleElement() {
        assertThat(solution.maxSubArray(new int[]{1})).isEqualTo(1);
    }

    @Test
    void example3_allPositive() {
        // entire array is the answer
        assertThat(solution.maxSubArray(new int[]{5, 4, 3, 2, 1})).isEqualTo(15);
    }

    @Test
    void edgeCase_allNegative() {
        // answer is the least negative — NOT zero
        assertThat(solution.maxSubArray(new int[]{-3, -1, -2})).isEqualTo(-1);
    }

    @Test
    void edgeCase_singleNegative() {
        assertThat(solution.maxSubArray(new int[]{-1})).isEqualTo(-1);
    }

    @Test
    void edgeCase_largeNegativeAtStart() {
        // [-100, 1, 2] → answer is 3, not -97
        assertThat(solution.maxSubArray(new int[]{-100, 1, 2})).isEqualTo(3);
    }

    @Test
    void edgeCase_largeNegativeAtEnd() {
        // [1, 2, -100] → answer is 3
        assertThat(solution.maxSubArray(new int[]{1, 2, -100})).isEqualTo(3);
    }

    @Test
    void edgeCase_alternatingPeaks() {
        // best subarray: [2,3] = 5 or just [5] = 5
        assertThat(solution.maxSubArray(new int[]{-1, 2, 3, -9, 5})).isEqualTo(5);
    }
}
