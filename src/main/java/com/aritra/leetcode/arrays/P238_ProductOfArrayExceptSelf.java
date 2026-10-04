package com.aritra.leetcode.arrays;

/**
 * Problem #238 — Product of Array Except Self
 * URL: https://leetcode.com/problems/product-of-array-except-self/
 * Difficulty: Medium
 * Pattern: Prefix Product + Running Suffix
 *
 * Time: O(n) — two passes through the array
 * Space: O(1) — output array doesn't count, only one extra variable (suffix)
 *
 * Constraint: No division allowed.
 *
 * Approach:
 * For any index i: answer[i] = (product of all elements LEFT of i)
 * × (product of all elements RIGHT of i)
 *
 * Pass 1 — Left to right: build prefix array where prefix[i] = product of
 * nums[0..i-1]
 * input: [1, 2, 3, 4]
 * prefix: [1, 1, 2, 6] ← prefix[0]=1 (nothing to the left)
 *
 * Pass 2 — Right to left: multiply each prefix[i] by a running suffix product
 * suffix starts at 1
 * i=3: answer[3] = prefix[3] × suffix(1) = 6 × 1 = 6, suffix = 1 × nums[3]=4 →
 * 4
 * i=2: answer[2] = prefix[2] × suffix(4) = 2 × 4 = 8, suffix = 4 × nums[2]=3 →
 * 12
 * i=1: answer[1] = prefix[1] × suffix(12) = 1 × 12 = 12, suffix = 12 ×
 * nums[1]=2 → 24
 * i=0: answer[0] = prefix[0] × suffix(24) = 1 × 24 = 24
 * result: [24, 12, 8, 6] ✅
 *
 * Aritra's first attempt (correct logic, O(n²) — TLE on large inputs):
 * for each i: multiply all elements left + multiply all elements right
 * separately
 * Problem: recalculates overlapping products from scratch every iteration
 */
public class P238_ProductOfArrayExceptSelf {

    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];

        // Pass 1 — build prefix products into answer array
        // answer[i] = product of all elements to the LEFT of i
        answer[0] = 1; // nothing to the left of index 0
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        // 1,-1,-1,0,0,0

        // Pass 2 — multiply by running suffix (right to left)
        // suffix = product of all elements to the RIGHT of current i
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] = answer[i] * suffix;
            suffix = suffix * nums[i]; // expand suffix leftward
        }

        return answer;
        // int[] answers = new int[nums.length];
        // for(int i = 0; i < nums.length; i++) {
        // int left = multiplyAll(nums, 0, i);
        // int right = multiplyAll(nums, i+1, nums.length);
        // answers[i] = left * right;
        // }
        // return answers;
    }

    private int multiplyAll(int[] nums, int start, int end) {
        int totalProduct = 1;
        for (int i = start; i < end; i++) {
            totalProduct = totalProduct * nums[i];
        }
        return totalProduct;
    }
}
