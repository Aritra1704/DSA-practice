package com.aritra.leetcode.arrays;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class P011_ContainerWithMostWaterTest {

    private final P011_ContainerWithMostWater solution = new P011_ContainerWithMostWater();

    @Test
    void example1_standard() {
        // best: index 1(h=8) and index 8(h=7) → min(8,7) × 7 = 49
        assertThat(solution.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}))
                .isEqualTo(49);
    }

    @Test
    void example2_twoElements() {
        assertThat(solution.maxArea(new int[]{1, 1})).isEqualTo(1);
    }

    @Test
    void edgeCase_increasingHeights() {
        // best container is the two tallest at the ends
        assertThat(solution.maxArea(new int[]{1, 2, 3, 4, 5})).isEqualTo(6);
    }

    @Test
    void edgeCase_decreasingHeights() {
        assertThat(solution.maxArea(new int[]{5, 4, 3, 2, 1})).isEqualTo(6);
    }

    @Test
    void edgeCase_allSameHeight() {
        // width = n-1, height = 1 → area = n-1
        assertThat(solution.maxArea(new int[]{3, 3, 3, 3})).isEqualTo(9);
    }

    @Test
    void edgeCase_tallWallsAtEnds() {
        // tall walls at both ends = widest container wins
        assertThat(solution.maxArea(new int[]{10, 1, 1, 1, 10})).isEqualTo(40);
    }

    @Test
    void edgeCase_tallWallInMiddle() {
        // tall middle wall doesn't help — container bounded by shorter side
        assertThat(solution.maxArea(new int[]{1, 100, 1})).isEqualTo(2);
    }
}
