package com.aritra.leetcode.arrays;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class P238_ProductOfArrayExceptSelfTest {

    private final P238_ProductOfArrayExceptSelf solution = new P238_ProductOfArrayExceptSelf();

    @Test
    void example1_standard() {
        // [1,2,3,4] → [24,12,8,6]
        assertThat(solution.productExceptSelf(new int[]{1, 2, 3, 4}))
                .containsExactly(24, 12, 8, 6);
    }

    @Test
    void example2_withNegatives() {
        // [-1,1,0,-3,3] → [0,0,9,0,0]
        assertThat(solution.productExceptSelf(new int[]{-1, 1, 0, -3, 3}))
                .containsExactly(0, 0, 9, 0, 0);
    }

    @Test
    void edgeCase_twoElements() {
        // [3, 4] → [4, 3]
        assertThat(solution.productExceptSelf(new int[]{3, 4}))
                .containsExactly(4, 3);
    }

    @Test
    void edgeCase_containsZero() {
        // [1,0,3,4] → [0,12,0,0]
        assertThat(solution.productExceptSelf(new int[]{1, 0, 3, 4}))
                .containsExactly(0, 12, 0, 0);
    }

    @Test
    void edgeCase_twoZeros() {
        // [0,0,3,4] → all zeros (two zeros means every product = 0)
        assertThat(solution.productExceptSelf(new int[]{0, 0, 3, 4}))
                .containsExactly(0, 0, 0, 0);
    }

    @Test
    void edgeCase_allOnes() {
        assertThat(solution.productExceptSelf(new int[]{1, 1, 1, 1}))
                .containsExactly(1, 1, 1, 1);
    }

    @Test
    void edgeCase_allNegatives() {
        // [-1,-2,-3,-4] → [-24,-12,-8,-6]
        assertThat(solution.productExceptSelf(new int[]{-1, -2, -3, -4}))
                .containsExactly(-24, -12, -8, -6);
    }
}
