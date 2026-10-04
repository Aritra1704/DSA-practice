package com.aritra.leetcode.arrays;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class P001_TwoSumTest {

    private final P001_TwoSum solution = new P001_TwoSum();

    @Test
    void example1_basicCase() {
        // [2,7,11,15], target=9 → [0,1] (nums[0]+nums[1]=2+7=9)
        assertThat(solution.twoSum(new int[]{2, 7, 11, 15}, 9))
                .containsExactlyInAnyOrder(0, 1);
    }

    @Test
    void example2_numberNotAtStart() {
        // [3,2,4], target=6 → [1,2] (nums[1]+nums[2]=2+4=6)
        assertThat(solution.twoSum(new int[]{3, 2, 4}, 6))
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void example3_duplicates() {
        // [3,3], target=6 → [0,1]
        assertThat(solution.twoSum(new int[]{3, 3}, 6))
                .containsExactlyInAnyOrder(0, 1);
    }

    @Test
    void edgeCase_twoElements() {
        assertThat(solution.twoSum(new int[]{1, 9}, 10))
                .containsExactlyInAnyOrder(0, 1);
    }

    @Test
    void edgeCase_negativeNumbers() {
        // [-3, 4, 3, 90], target=0 → [0,2]
        assertThat(solution.twoSum(new int[]{-3, 4, 3, 90}, 0))
                .containsExactlyInAnyOrder(0, 2);
    }
}
