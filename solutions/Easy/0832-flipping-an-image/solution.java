// ──────────────────────────────────────────────────
// Problem  : 832. Flipping an Image
// Difficulty: Easy
// Tags     : Array, Two Pointers, Bit Manipulation, Matrix, Simulation
// Link     : https://leetcode.com/problems/flipping-an-image/
// Runtime  : 0 ms (beats 100%)
// Memory   : 45964000 (beats 18%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[][] flipAndInvertImage(int[][] image) {
        for(int[] line : image) {
            if(line.length % 2 == 1) line[line.length / 2] ^= 1;
            for(int i = 0; i < line.length/2; i++) {
                int temp = line[i];
                line[i] = 1 ^ line[line.length-i-1];
                line[line.length-i-1] = 1 ^ temp;
            }
        }
        return image;
    }
}