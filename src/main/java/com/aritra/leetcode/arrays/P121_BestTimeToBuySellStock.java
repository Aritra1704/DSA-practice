package com.aritra.leetcode.arrays;

/**
 * Problem #121 — Best Time to Buy and Sell Stock
 * URL: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 * Difficulty: Easy
 * Pattern: Greedy / Sliding Window (track running minimum)
 *
 * Time: O(n) — single pass through prices array
 * Space: O(1) — only two variables regardless of input size
 *
 * Approach:
 * Track the minimum price seen so far as we scan left to right.
 * At each price, calculate profit if we sold today (price - minPrice).
 * Keep the maximum of all those profits.
 * We never need to look back — if a cheaper buy day exists, we already tracked
 * it.
 *
 * Aritra's brute force (O(n²)) — correct but too slow for FAANG:
 * for(int i = 0; i < prices.length; i++)
 * for(int j = i+1; j < prices.length; j++)
 * if(prices[i] < prices[j] && (prices[j] - prices[i]) > result)
 * result = prices[j] - prices[i];
 */
public class P121_BestTimeToBuySellStock {

    public int maxProfit(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < minPrice) {
                minPrice = prices[i];           // found a cheaper buy day
            } else {
                maxProfit = Math.max(maxProfit, prices[i] - minPrice); // best sell today?
            }
        }
        return maxProfit;
    }
}
