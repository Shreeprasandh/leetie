// ──────────────────────────────────────────────────
// Problem  : 115. Distinct Subsequences
// Difficulty: Hard
// Tags     : String, Dynamic Programming
// Link     : https://leetcode.com/problems/distinct-subsequences/
// Runtime  : 20 ms (beats 53%)
// Memory   : 66764000 (beats 6%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numDistinct(String s, String t) {
        int m = s.length();
        int n = t.length();
        double[][] dp = new double[n + 1][m + 1];
        for(int j = 0; j <= m; j++){
            dp[0][j] = 1;
        }
        for(int i = 1; i <= n ; i++){
            for(int j = 1; j <= m; j++){
                if(t.charAt(i - 1) == s.charAt(j - 1)){
                    dp[i][j] = dp[i - 1][j - 1] + dp[i][j - 1];
                }else { 
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return (int) dp[n][m];
    }
}