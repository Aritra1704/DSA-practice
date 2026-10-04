package com.aritra.leetcode.arrays;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class P121_BestTimeToBuySellStockTest {

    private final P121_BestTimeToBuySellStock solution = new P121_BestTimeToBuySellStock();

    @Test
    void example1_standardCase() {
        // Buy at 1 (day 2), sell at 6 (day 5) → profit = 5
        assertThat(solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4})).isEqualTo(5);
    }

    @Test
    void example2_pricesOnlyDecrease_noProfit() {
        // Prices go down every day — no profitable trade possible
        assertThat(solution.maxProfit(new int[]{7, 6, 4, 3, 1})).isEqualTo(0);
    }

    @Test
    void example3_buyLowAfterHigh() {
        // Buy at 1 (day 2), sell at 7 (day 4) → profit = 6
        assertThat(solution.maxProfit(new int[]{2, 4, 1, 7})).isEqualTo(6);
    }

    @Test
    void example4_bestSellNotAtEnd() {
        // Buy at 1, sell at 5 — not the last element
        assertThat(solution.maxProfit(new int[]{3, 1, 5, 2, 4})).isEqualTo(4);
    }

    @Test
    void edgeCase_singleElement() {
        // Can't buy and sell same day → 0
        assertThat(solution.maxProfit(new int[]{5})).isEqualTo(0);
    }

    @Test
    void edgeCase_twoElements_profit() {
        assertThat(solution.maxProfit(new int[]{1, 9})).isEqualTo(8);
    }

    @Test
    void edgeCase_twoElements_noProfit() {
        assertThat(solution.maxProfit(new int[]{9, 1})).isEqualTo(0);
    }

    @Test
    void edgeCase_allSamePrice() {
        // No profit if all prices equal
        assertThat(solution.maxProfit(new int[]{5, 5, 5, 5})).isEqualTo(0);
    }

    @Test
    void edgeCase_profitAtVeryEnd() {
        // Min is early, max is at the last element
        assertThat(solution.maxProfit(new int[]{3, 1, 5, 2, 4, 1, 7})).isEqualTo(6);
    }
}
