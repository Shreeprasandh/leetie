// ──────────────────────────────────────────────────
// Problem  : 647. Palindromic Substrings
// Difficulty: Medium
// Tags     : Two Pointers, String, Dynamic Programming
// Link     : https://leetcode.com/problems/palindromic-substrings/
// Runtime  : 5 ms (beats 98%)
// Memory   : 43132000 (beats 33%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int countSubstrings(String s) {
        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            // Case 1: Odd length palindromes (center is at a character)
            count += expand(s, i, i);
            // Case 2: Even length palindromes (center is between two characters)
            count += expand(s, i, i + 1);
        }
        return count;
    }

    private int expand(String s, int left, int right) {
        int count = 0;
        // Expand outwards as long as characters match and pointers are in bounds
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            count++;
            left--;
            right++;
        }
        return count;
    }
}