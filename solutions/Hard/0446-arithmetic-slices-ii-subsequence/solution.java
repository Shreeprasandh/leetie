// ──────────────────────────────────────────────────
// Problem  : 446. Arithmetic Slices II - Subsequence
// Difficulty: Hard
// Tags     : Array, Dynamic Programming
// Link     : https://leetcode.com/problems/arithmetic-slices-ii-subsequence/
// Runtime  : 135 ms (beats 81%)
// Memory   : 86012000 (beats 92%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numberOfArithmeticSlices(int[] nums) {
        int n = nums.length;
        if (n < 3) return 0;
        
        int totalSlices = 0;
        java.util.Map<Integer, Integer>[] dp = new java.util.Map[n];
        
        for (int i = 0; i < n; i++) {
            dp[i] = new java.util.HashMap<>();
            for (int j = 0; j < i; j++) {
                long diff = (long) nums[i] - nums[j];
                if (diff < Integer.MIN_VALUE || diff > Integer.MAX_VALUE) {
                    continue;
                }
                
                int d = (int) diff;
                int count = dp[j].getOrDefault(d, 0);
                
                totalSlices += count;
                dp[i].put(d, dp[i].getOrDefault(d, 0) + count + 1);
            }
        }
        
        return totalSlices;
    }
}