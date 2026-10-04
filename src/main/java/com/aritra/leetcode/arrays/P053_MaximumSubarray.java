package com.aritra.leetcode.arrays;

/**
 * Problem #53 — Maximum Subarray
 * URL: https://leetcode.com/problems/maximum-subarray/
 * Difficulty: Medium
 * Pattern: Kadane's Algorithm
 *
 * Time:  O(n)  — single pass
 * Space: O(1)  — two variables only
 *
 * Approach:
 * At each index, decide: extend the current subarray OR start fresh.
 * Start fresh when currentSum goes negative — a negative prefix only hurts.
 * Track the global maximum across all positions.
 *
 * Key insight: initialise both currentSum and maxSum to nums[0], start loop
 * at i=1. This correctly handles all-negative arrays (answer is least negative,
 * not 0).
 *
 * Equivalent one-liner for the core decision:
 *   currentSum = Math.max(nums[i], currentSum + nums[i]);
 *
 * Aritra's solution — first attempt, clean and correct:
 */
public class P053_MaximumSubarray {

    public int maxSubArray(int[] nums) {
        int currentSum = nums[0];
        int maxSum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (currentSum < 0) {
                currentSum = nums[i];       // start fresh — negative prefix is a drag
            } else {
                currentSum += nums[i];      // extend current subarray
            }
            if (currentSum > maxSum) {
                maxSum = currentSum;        // track global best
            }
        }
        return maxSum;
    }
}
