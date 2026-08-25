// ──────────────────────────────────────────────────
// Problem  : 201. Bitwise AND of Numbers Range
// Difficulty: Medium
// Tags     : Bit Manipulation
// Link     : https://leetcode.com/problems/bitwise-and-of-numbers-range/
// Runtime  : 3 ms (beats 100%)
// Memory   : 45908000 (beats 38%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int rangeBitwiseAnd(int left, int right) {
        int shift = 0;
        while (left < right) {
            left >>= 1;
            right >>= 1;
            shift++;
        }
        return left << shift;
    }
}