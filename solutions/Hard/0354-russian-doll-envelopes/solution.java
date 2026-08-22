// ──────────────────────────────────────────────────
// Problem  : 354. Russian Doll Envelopes
// Difficulty: Hard
// Tags     : Array, Binary Search, Dynamic Programming, Sorting, Longest Increasing Subsequence
// Link     : https://leetcode.com/problems/russian-doll-envelopes/
// Runtime  : 39 ms (beats 83%)
// Memory   : 99348000 (beats 70%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.Arrays;

class Solution {
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) return 0;
        
        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(b[1], a[1]);
            }
            return Integer.compare(a[0], b[0]);
        });
        
        int[] dp = new int[envelopes.length];
        int len = 0;
        
        for (int[] env : envelopes) {
            int height = env[1];
            int idx = Arrays.binarySearch(dp, 0, len, height);
            if (idx < 0) {
                idx = -(idx + 1);
            }
            dp[idx] = height;
            if (idx == len) {
                len++;
            }
        }
        
        return len;
    }
}