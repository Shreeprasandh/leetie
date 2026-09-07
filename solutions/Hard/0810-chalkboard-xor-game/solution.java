// ──────────────────────────────────────────────────
// Problem  : 810. Chalkboard XOR Game
// Difficulty: Hard
// Tags     : Array, Math, Bit Manipulation, Brainteaser, Game Theory, Zero-Sum Game, Impartial Game
// Link     : https://leetcode.com/problems/chalkboard-xor-game/
// Runtime  : 0 ms (beats 100%)
// Memory   : 46524000 (beats 16%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean xorGame(int[] nums) {
        int xor = 0;

        for (int num : nums) {
            xor ^= num;
        }

        // Alice wins if the total XOR is already 0, 
        // or if she starts with an even number of elements.
        return xor == 0 || nums.length % 2 == 0;
    }
}