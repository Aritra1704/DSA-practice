package com.aritra.leetcode.arrays;

/**
 * Problem #11 — Container With Most Water
 * URL: https://leetcode.com/problems/container-with-most-water/
 * Difficulty: Medium
 * Pattern: Two Pointers
 *
 * Time: O(n) — single pass, two pointers moving toward each other
 * Space: O(1) — no extra space
 *
 * Problem:
 * Given heights array, find two lines that form a container holding max water.
 * Water = min(height[left], height[right]) × (right - left)
 *
 * Approach:
 * Start with widest possible container (left=0, right=n-1).
 * Move the pointer with the SHORTER height inward — only chance to find more
 * water
 * is a taller line. Moving the taller pointer can only make things worse (width
 * decreases, height bounded by the shorter side anyway).
 *
 * Trace on [1,8,6,2,5,4,8,3,7]:
 * left=0(h=1), right=8(h=7) → water=1×8=8, move left (shorter)
 * left=1(h=8), right=8(h=7) → water=7×7=49, move right (shorter)
 * left=1(h=8), right=7(h=3) → water=3×6=18, move right
 * left=1(h=8), right=6(h=8) → water=8×5=40, move either
 * ... → max=49 ✅
 */
public class P011_ContainerWithMostWater {

    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int water = Math.min(height[left], height[right]) * (right - left);
            maxWater = Math.max(maxWater, water);

            // move the shorter pointer — moving taller can only reduce area
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxWater;

        // int left = 0;
        // int right = height.length - 1;
        // int maxWater = 0;
        // while(left < right) {
        // int water = (right - left) * (height[left] < height[right] ? height[left] :
        // height[right]);
        // maxWater = maxWater > water ? maxWater : water;

        // if(height[left] < height[right]) {
        // left++;
        // } else {
        // right--;
        // }
        // }
        // return maxWater;
    }
}
