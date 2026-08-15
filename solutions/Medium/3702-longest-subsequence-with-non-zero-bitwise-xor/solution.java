// ──────────────────────────────────────────────────
// Problem  : 3702. Longest Subsequence With Non-Zero Bitwise XOR
// Difficulty: Medium
// Tags     : N/A
// Link     : https://leetcode.com/problems/longest-subsequence-with-non-zero-bitwise-xor/
// Runtime  : 1 ms (beats 100%)
// Memory   : 133500000 (beats 52%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int longestSubsequence(int[] nums) {
        int xorSum = 0;
        boolean allZero = true;
        for (int num : nums) {
            xorSum ^= num;
            if (num != 0) allZero = false;
        }
        if (allZero) return 0;
        return xorSum != 0 ? nums.length : nums.length - 1;
    }
}