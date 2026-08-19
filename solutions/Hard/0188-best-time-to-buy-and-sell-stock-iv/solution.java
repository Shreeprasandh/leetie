// ──────────────────────────────────────────────────
// Problem  : 188. Best Time to Buy and Sell Stock IV
// Difficulty: Hard
// Tags     : Array, Dynamic Programming
// Link     : https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
// Runtime  : 8 ms (beats 28%)
// Memory   : 45240000 (beats 46%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxProfit(int k, int[] prices) {
        int n = prices.length;

        int[][][] dp = new int[n][2][k+1];

        for(int i=0;i<n;i++){
            for(int j=0;j<2;j++){
                Arrays.fill(dp[i][j], -1);
            }
        }

        return helper(0, 1, k, prices, dp);
    }

    private int helper(int idx, int canBuy, int k, int[] prices, int[][][] dp){
        if(idx == prices.length) return 0;
        if(k == 0) return 0;

        if(dp[idx][canBuy][k] != -1) return dp[idx][canBuy][k];

        int profit = 0;

        if(canBuy == 1){
            profit = Math.max(-prices[idx] + helper(idx+1, 0, k, prices, dp), 0 + helper(idx+1, 1, k, prices, dp));
        }
        else{
            profit = Math.max(prices[idx] + helper(idx+1, 1, k-1, prices, dp), 0 + helper(idx+1, 0, k, prices, dp));
        }

        return dp[idx][canBuy][k] = profit;
    }
}