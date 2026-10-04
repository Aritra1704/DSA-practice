package com.aritra.leetcode.arrays;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class P217_ContainsDuplicateTest {

    private final P217_ContainsDuplicate solution = new P217_ContainsDuplicate();

    @Test
    void example1_hasDuplicate() {
        assertThat(solution.containsDuplicate(new int[]{1, 2, 3, 1})).isTrue();
    }

    @Test
    void example2_allUnique() {
        assertThat(solution.containsDuplicate(new int[]{1, 2, 3, 4})).isFalse();
    }

    @Test
    void example3_multipleDuplicates() {
        assertThat(solution.containsDuplicate(new int[]{1, 1, 1, 3, 3})).isTrue();
    }

    @Test
    void edgeCase_singleElement() {
        assertThat(solution.containsDuplicate(new int[]{1})).isFalse();
    }

    @Test
    void edgeCase_twoSame() {
        assertThat(solution.containsDuplicate(new int[]{5, 5})).isTrue();
    }

    @Test
    void edgeCase_twoDifferent() {
        assertThat(solution.containsDuplicate(new int[]{5, 6})).isFalse();
    }

    @Test
    void edgeCase_duplicateAtEnd() {
        // Early exit shouldn't matter — duplicate is last
        assertThat(solution.containsDuplicate(new int[]{1, 2, 3, 4, 5, 1})).isTrue();
    }

    @Test
    void edgeCase_negativeNumbers() {
        assertThat(solution.containsDuplicate(new int[]{-1, -2, -3, -1})).isTrue();
    }
}
