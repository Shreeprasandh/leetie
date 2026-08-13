// ──────────────────────────────────────────────────
// Problem  : 5. Longest Palindromic Substring
// Difficulty: Medium
// Tags     : N/A
// Link     : https://leetcode.com/problems/longest-palindromic-substring/
// Runtime  : 6 ms (beats 99%)
// Memory   : 43664000 (beats 58%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    private int start = 0;
    private int maxLen = 0;

    public String longestPalindrome(String s) {
        if (s.length() < 2) {
            return s;
        }

        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            expand(chars, i, i);
            expand(chars, i, i + 1);
        }

        return s.substring(start, start + maxLen);
    }

    private void expand(char[] chars, int left, int right) {
        while (left >= 0 && right < chars.length && chars[left] == chars[right]) {
            left--;
            right++;
        }
        
        if (maxLen < right - left - 1) {
            start = left + 1;
            maxLen = right - left - 1;
        }
    }
}