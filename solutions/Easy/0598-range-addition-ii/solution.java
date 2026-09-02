// ──────────────────────────────────────────────────
// Problem  : 598. Range Addition II
// Difficulty: Easy
// Tags     : Array, Math
// Link     : https://leetcode.com/problems/range-addition-ii/
// Runtime  : 1 ms (beats 31%)
// Memory   : 48388000 (beats 90%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxCount(int m, int n, int[][] ops) {
        int a = m;
        int b = n;
        for(int i = 0; i < ops.length; i++){
            int x = ops[i][0];
            int y = ops[i][1];

            a = Math.min(a, x);
            b = Math.min(b, y);
        }
        return a * b;
    }
}