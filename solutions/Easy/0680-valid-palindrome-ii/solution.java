// ──────────────────────────────────────────────────
// Problem  : 680. Valid Palindrome II
// Difficulty: Easy
// Tags     : Two Pointers, String, Greedy
// Link     : https://leetcode.com/problems/valid-palindrome-ii/
// Runtime  : 4 ms (beats 99%)
// Memory   : 47696000 (beats 69%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean validPalindrome(String s) {

        int i = 0;
        int j = s.length() - 1;

        while (i < j) {

            if (s.charAt(i) != s.charAt(j)) {

                return isPalindrome(s, i + 1, j) ||
                       isPalindrome(s, i, j - 1);
            }

            i++;
            j--;
        }

        return true;
    }

    private boolean isPalindrome(String s, int i, int j) {

        while (i < j) {

            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }

            i++;
            j--;
        }

        return true;
    }
}