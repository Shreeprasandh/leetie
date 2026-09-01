// ──────────────────────────────────────────────────
// Problem  : 526. Beautiful Arrangement
// Difficulty: Medium
// Tags     : Array, Dynamic Programming, Backtracking, Bit Manipulation, Bitmask
// Link     : https://leetcode.com/problems/beautiful-arrangement/
// Runtime  : 37 ms (beats 77%)
// Memory   : 41808000 (beats 87%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    int res = 0;
    public int countArrangement(int n) {
        if (n == 0) return 0;
        backtrack(1, n, new int[n+1]);
        return res;
    }

    private void backtrack(int pos, int n, int[] usedValueArr) {
        if (pos > n) {
            res++;
            return;
        }
        for (int i = 1; i <= n; i++) {
            if (usedValueArr[i] == 0 && (pos % i == 0 || i % pos == 0)) {
                usedValueArr[i] = 1;
                backtrack(pos+1, n, usedValueArr);
                usedValueArr[i] = 0;
            }
        }
    }
}