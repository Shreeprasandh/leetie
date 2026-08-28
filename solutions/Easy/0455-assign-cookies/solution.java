// ──────────────────────────────────────────────────
// Problem  : 455. Assign Cookies
// Difficulty: Easy
// Tags     : Array, Two Pointers, Greedy, Sorting, Quicksort
// Link     : https://leetcode.com/problems/assign-cookies/
// Runtime  : 15 ms (beats 60%)
// Memory   : 51464000 (beats 56%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findContentChildren(int[] g, int[] s) {

        Arrays.sort(g);
        Arrays.sort(s);

        int children = 0;
        int cookie = 0;

        while (cookie < s.length && children < g.length) {

            if (s[cookie] >= g[children]) {
                children++;
            }

            cookie++;
        }

        return children;
    }
}