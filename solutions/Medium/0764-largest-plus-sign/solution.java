// ──────────────────────────────────────────────────
// Problem  : 764. Largest Plus Sign
// Difficulty: Medium
// Tags     : Array, Dynamic Programming
// Link     : https://leetcode.com/problems/largest-plus-sign/
// Runtime  : 18 ms (beats 99%)
// Memory   : 48204000 (beats 75%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int orderOfLargestPlusSign(int n, int[][] mines) {
        int dp[][] = new int[n][n];
        for(int mine[] : mines) {
            int x = mine[0], y = mine[1];
            dp[x][y] = -1;
        }

        for(int i=0; i<n; ++i) {
            int prefix = 0;
            for(int j=0; j<n; ++j) {
                if(dp[i][j] == -1) {
                    prefix = 0;
                    continue;
                }
                ++prefix;
                dp[i][j] = prefix;
            }
            int suffix = 0;
            for(int j=n-1; j>=0; --j) {
                if(dp[i][j] == -1) {
                    suffix = 0;
                    continue;
                }
                ++suffix;
                dp[i][j] = Math.min(dp[i][j], suffix);
            }
        }

        int maxPlus = 0;
        for(int j=0; j<n; ++j) {
            int prefix = 0;
            for(int i=0; i<n; ++i) {
                if(dp[i][j] == -1) {
                    prefix = 0;
                    continue;
                }
                ++prefix;
                dp[i][j] = Math.min(dp[i][j], prefix);
            }
            int suffix = 0;
            for(int i=n-1; i>=0; --i) {
                if(dp[i][j] == -1) {
                    suffix = 0;
                    continue;
                }
                ++suffix;
                dp[i][j] = Math.min(dp[i][j], suffix);
                maxPlus = Math.max(maxPlus, dp[i][j]);
            }
        }

        return maxPlus;
    }
}