// ──────────────────────────────────────────────────
// Problem  : 1927. Sum Game
// Difficulty: Medium
// Tags     : Math, String, Greedy, Game Theory
// Link     : https://leetcode.com/problems/sum-game/
// Runtime  : 13 ms (beats 5%)
// Memory   : 47052000 (beats 42%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean sumGame(String num) {
        int[] sum = {0, 0}, q = {0, 0};
        int n = num.length();

        for (int i = 0; i < n; i++) {
            int j = i / (n >> 1);
            if (num.charAt(i) == '?')
                q[j]++;
            else
                sum[j] += num.charAt(i) - '0';
        }

        return ((q[0] + q[1]) % 2 == 1) || 
               ((sum[0] - sum[1]) << 1) != (q[1] - q[0]) * 9;
    }
}