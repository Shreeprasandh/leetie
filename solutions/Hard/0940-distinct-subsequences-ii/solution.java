// ──────────────────────────────────────────────────
// Problem  : 940. Distinct Subsequences II
// Difficulty: Hard
// Tags     : String, Dynamic Programming
// Link     : https://leetcode.com/problems/distinct-subsequences-ii/
// Runtime  : 4 ms (beats 71%)
// Memory   : 42840000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int distinctSubseqII(String s) {
        int n = s.length();
        int MOD = (int) 1e9 + 7;

        int[] dp = new int[n];
        int[] countEndWith = new int[26];

        int sum = 0;

        for (int i = 0; i < n; i++) {
            int idx = s.charAt(i) - 'a';

            dp[i] = (int) ((1L + sum - countEndWith[idx] + MOD) % MOD);

            sum = (sum + dp[i]) % MOD;

            countEndWith[idx] = (countEndWith[idx] + dp[i]) % MOD;
        }

        return sum;
    }
}