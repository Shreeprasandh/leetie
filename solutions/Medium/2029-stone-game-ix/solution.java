// ──────────────────────────────────────────────────
// Problem  : 2029. Stone Game IX
// Difficulty: Medium
// Tags     : N/A
// Link     : https://leetcode.com/problems/stone-game-ix/
// Runtime  : 7 ms (beats 29%)
// Memory   : 114700000 (beats 82%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean stoneGameIX(int[] stones) {

        int c0 = 0;
        int c1 = 0;
        int c2 = 0;

        // Count stones according to their remainder
        for (int stone : stones) {

            if (stone % 3 == 0) {
                c0++;
            } 
            else if (stone % 3 == 1) {
                c1++;
            } 
            else {
                c2++;
            }
        }

        // Case 1: Number of remainder-0 stones is even
        if (c0 % 2 == 0) {
            return c1 > 0 && c2 > 0;
        }

        // Case 2: Number of remainder-0 stones is odd
        return Math.abs(c1 - c2) > 2;
    }
}