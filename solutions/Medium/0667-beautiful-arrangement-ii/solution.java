// ──────────────────────────────────────────────────
// Problem  : 667. Beautiful Arrangement II
// Difficulty: Medium
// Tags     : Array, Math
// Link     : https://leetcode.com/problems/beautiful-arrangement-ii/
// Runtime  : 1 ms (beats 100%)
// Memory   : 46888000 (beats 12%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] constructArray(int n, int k) {
        int[] ans = new int[n];
        for (int i = 0, a = 1, z = k + 1; i <= k; i++)
            ans[i] = i % 2 == 1 ? z-- : a++;
        for (int i = k+1; i < n;)
            ans[i] = ++i;
        return ans;
    }
}