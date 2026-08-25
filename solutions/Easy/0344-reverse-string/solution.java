// ──────────────────────────────────────────────────
// Problem  : 344. Reverse String
// Difficulty: Easy
// Tags     : Two Pointers, String
// Link     : https://leetcode.com/problems/reverse-string/
// Runtime  : 0 ms (beats 100%)
// Memory   : 48036000 (beats 88%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public void reverseString(char[] s) {
        for (int i = 0, j = s.length - 1; i < j; i++, j--) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
    }
}