// ──────────────────────────────────────────────────
// Problem  : 717. 1-bit and 2-bit Characters
// Difficulty: Easy
// Tags     : Array
// Link     : https://leetcode.com/problems/1-bit-and-2-bit-characters/
// Runtime  : 0 ms (beats 100%)
// Memory   : 44556000 (beats 28%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean isOneBitCharacter(int[] bits) {
        for(int i = 0; i < bits.length; i++) {
            if(i == bits.length - 1) return true;
            if(bits[i] == 1) i++;
        }
        return false;
    }
}