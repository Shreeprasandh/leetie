// ──────────────────────────────────────────────────
// Problem  : 521. Longest Uncommon Subsequence I
// Difficulty: Easy
// Tags     : String
// Link     : https://leetcode.com/problems/longest-uncommon-subsequence-i/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42852000 (beats 20%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findLUSlength(String a, String b) {
        if (a.equals(b)) return -1;
        return Math.max(a.length(), b.length());
    }
}