// ──────────────────────────────────────────────────
// Problem  : 918. Maximum Sum Circular Subarray
// Difficulty: Medium
// Tags     : Array, Divide and Conquer, Dynamic Programming, Queue, Monotonic Queue
// Link     : https://leetcode.com/problems/maximum-sum-circular-subarray/
// Runtime  : 7 ms (beats 51%)
// Memory   : 50540000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxSubarraySumCircular(int[] nums) {
        int currMin = nums[0], currMax = nums[0], minSum = nums[0], maxSum = nums[0];
        int totalSum = nums[0];
        for(int i=1;i<nums.length;i++)
        {
            currMax = Math.max(nums[i],currMax+nums[i]);
            maxSum = Math.max(maxSum,currMax);

            currMin = Math.min(nums[i],currMin+nums[i]);
            minSum = Math.min(currMin,minSum);

            totalSum += nums[i];
        }
        if(minSum == totalSum) return maxSum;
        return Math.max(maxSum, totalSum - minSum);
    }
}