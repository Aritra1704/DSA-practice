package com.aritra.leetcode.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem #1 — Two Sum
 * URL: https://leetcode.com/problems/two-sum/
 * Difficulty: Easy
 * Pattern: HashMap (Complement lookup)
 *
 * Time: O(n) — single pass through array
 * Space: O(n) — HashMap stores up to n elements
 *
 * Approach:
 * For each number, check if its complement (target - num) exists in the map.
 * If yes — found the pair. If no — store current number with its index.
 * One pass, no nested loop needed.
 */
public class P001_TwoSum {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(); // value → index

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];

            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }

            map.put(nums[i], i);
        }

        return new int[] {}; // no solution found (problem guarantees one exists)

        // Map<Integer, Integer> map = new HashMap<>();
        // for (int i = 0; i < nums.length; i++) {
        // int complement = target - nums[i];
        // if (map.containsKey(complement)) {
        // return new int[] { map.get(complement), i };
        // }
        // map.put(nums[i], i);
        // }
        // return new int[0];
        // }

        // for(int i = 0; i<nums.length;i++) {
        // for(int j = i+1; j<nums.length;j++) {
        // if(target == (nums[i] + nums[j])) {
        // return new int[]{i,j};
        // }
        // }
        // }
        // return null;

        // Map<Integer, Integer> substract = new HashMap<>();
        // for(int i = 0; i<nums.length;i++) {
        // substract.put(target - nums[i], i);
        // }
        // for(int i = 0; i <nums.length;i++) {
        // if(substract.get(nums[i]) != null && substract.get(nums[i]) != i) {
        // return new int[]{i, substract.get(nums[i])};
        // }
        // }
        // return null;
    }
}
