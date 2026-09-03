// ──────────────────────────────────────────────────
// Problem  : 643. Maximum Average Subarray I
// Difficulty: Easy
// Tags     : Array, Sliding Window
// Link     : https://leetcode.com/problems/maximum-average-subarray-i/
// Runtime  : 7 ms (beats 7%)
// Memory   : 69468000 (beats 80%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public double findMaxAverage(int[] nums, int k) {
        double max = Integer.MIN_VALUE;
        int l = 0, r = l;
        int sum = 0;
        while (r < nums.length) {
            while (l < r && r - l + 1 > k) {
                sum -= nums[l];
                l++;
            }
            
            sum += nums[r];
            if (r - l + 1 == k) 
                max = Math.max(max, ((double) sum / k));

            r++;
        }
        return max;
    }
}