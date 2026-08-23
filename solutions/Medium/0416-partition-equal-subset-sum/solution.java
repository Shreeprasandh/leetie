// ──────────────────────────────────────────────────
// Problem  : 416. Partition Equal Subset Sum
// Difficulty: Medium
// Tags     : Array, Dynamic Programming, Knapsack Problem, 0-1 Knapsack
// Link     : https://leetcode.com/problems/partition-equal-subset-sum/
// Runtime  : 31 ms (beats 82%)
// Memory   : 44048000 (beats 73%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        if (totalSum % 2 != 0) {
            return false;
        }

        int target = totalSum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;

        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
        }

        return dp[target];
    }
}