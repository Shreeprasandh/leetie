// ──────────────────────────────────────────────────
// Problem  : 389. Find the Difference
// Difficulty: Easy
// Tags     : Hash Table, String, Bit Manipulation, Sorting
// Link     : https://leetcode.com/problems/find-the-difference/
// Runtime  : 1 ms (beats 100%)
// Memory   : 43072000 (beats 72%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public char findTheDifference(String s, String t) {
        char c = 0;
        for(char cs : s.toCharArray()) c ^= cs;
        for(char ct : t.toCharArray()) c ^= ct;
        return c;
    }
}