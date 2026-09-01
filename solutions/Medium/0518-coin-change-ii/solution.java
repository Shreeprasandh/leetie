// ──────────────────────────────────────────────────
// Problem  : 518. Coin Change II
// Difficulty: Medium
// Tags     : Array, Dynamic Programming, Knapsack Problem, Complete Knapsack
// Link     : https://leetcode.com/problems/coin-change-ii/
// Runtime  : 10 ms (beats 67%)
// Memory   : 56656000 (beats 47%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int change(int amount, int[] coins) {
        int n = coins.length;
        int[][] dp = new int[n + 1][amount + 1];

        // Base case: 1 way to make amount 0 (empty set)
        for (int i = 0; i <= n; i++) dp[i][0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= amount; j++) {
                if (coins[i - 1] <= j) {
                    // Inclusion: use current coin (dp[i]) + Exclusion: skip current coin (dp[i-1])
                    dp[i][j] = dp[i][j - coins[i - 1]] + dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n][amount];
    }
}