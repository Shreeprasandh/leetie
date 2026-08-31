// ──────────────────────────────────────────────────
// Problem  : 477. Total Hamming Distance
// Difficulty: Medium
// Tags     : Array, Math, Bit Manipulation
// Link     : https://leetcode.com/problems/total-hamming-distance/
// Runtime  : 4 ms (beats 98%)
// Memory   : 48100000 (beats 51%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int totalHammingDistance(int[] nums) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            int bit = 0;
            for (int num : nums) bit += (num >> i) & 1;
            result += bit * (nums.length - bit);
        }
        return result;
    }
}