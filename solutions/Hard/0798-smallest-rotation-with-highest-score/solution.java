// ──────────────────────────────────────────────────
// Problem  : 798. Smallest Rotation with Highest Score
// Difficulty: Hard
// Tags     : Array, Prefix Sum
// Link     : https://leetcode.com/problems/smallest-rotation-with-highest-score/
// Runtime  : 4 ms (beats 98%)
// Memory   : 77104000 (beats 50%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int bestRotation(int[] nums) {
        final int size = nums.length;
        int[] rsc = new int[size];
        for(int i = 0; i < size - 1; i++) {
            int value = nums[i];
            int downPos = (i + 1 + size - value) % size;
            rsc[downPos]--;
        }
        int value = nums[size-1];
        if( value != 0 ) rsc[size - value]--;
        int bestk = 0;
        int bestscore = rsc[0];
        int score = rsc[0];
        for(int i = 1; i < nums.length; i++) {
            score += rsc[i] + 1;
            if( score > bestscore ) {
                bestk = i;
                bestscore = score;
            }
        }
        return bestk;
    }
}