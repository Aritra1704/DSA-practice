package com.aritra;

import java.util.Arrays;

public class practice {
    public static void main(String[] args) {
        int[] input = new int[] { -2, 1, -3, 4, -1, 2, 1, -5, 4 };
        int result = maxSubArray(input);
        System.out.println("Result: " + result);

        Arrays.sort(input);
        System.out.println(Arrays.toString(input));
    }

    public static int maxSubArray(int[] nums) {
        int currentSum = nums[0];
        int maxSum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (currentSum < 0) {
                currentSum = nums[i];
            } else {
                currentSum += nums[i];
            }
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }
        return maxSum;
    }
}
