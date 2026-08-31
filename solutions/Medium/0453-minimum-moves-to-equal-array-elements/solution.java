// ──────────────────────────────────────────────────
// Problem  : 453. Minimum Moves to Equal Array Elements
// Difficulty: Medium
// Tags     : Array, Math
// Link     : https://leetcode.com/problems/minimum-moves-to-equal-array-elements/
// Runtime  : 2 ms (beats 91%)
// Memory   : 47224000 (beats 51%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int minMoves(int[] nums) {
        int mn = Integer.MAX_VALUE;

        for (int num : nums) {
            mn = Math.min(mn, num);
        }

        int moves = 0;

        for (int num : nums) {
            moves += num - mn;
        }

        return moves;
    }
}