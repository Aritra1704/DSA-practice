package com.aritra.leetcode.arrays;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem #217 — Contains Duplicate
 * URL: https://leetcode.com/problems/contains-duplicate/
 * Difficulty: Easy
 * Pattern: HashSet (early exit)
 *
 * Time: O(n) — single pass
 * Space: O(n) — HashSet stores up to n elements
 *
 * Approach:
 * Add each number to a HashSet. HashSet.add() returns false if the element
 * already exists — return true immediately (duplicate found, early exit).
 * Much better than scanning full array and comparing sizes.
 *
 * Aritra's first attempt (correct but no early exit):
 * Add all to HashSet, then compare sizes — O(n) but always full scan.
 *
 * Key trick: HashSet.add() returns false if already present — use it.
 */
public class P217_ContainsDuplicate {

    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) { // add() returns false = already exists = duplicate
                return true;
            }
        }
        return false;

        // Set<Integer> distinct = new HashSet<>();
        // for(int i = 0; i < nums.length; i++) {
        // distinct.add(nums[i]);
        // }
        // if(nums.length == distinct.size()) {
        // return false;
        // }
        // return true;

        // Set<Integer> distinct = new HashSet<>();
        // for(int i = 0; i < nums.length; i++) {
        // if(!distinct.contains(nums[i])) {
        // distinct.add(nums[i]);
        // } else {
        // return true;
        // }

        // }
        // return false;

        // Set<Integer> distinct = new HashSet<>();
        // for(int i = 0; i < nums.length; i++) {
        // if(!distinct.add(nums[i])) {
        // return true;
        // }
        // }
        // return false;
    }
}
