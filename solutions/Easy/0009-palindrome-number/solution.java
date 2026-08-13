// ──────────────────────────────────────────────────
// Problem  : 9. Palindrome Number
// Difficulty: Easy
// Tags     : N/A
// Link     : https://leetcode.com/problems/palindrome-number/
// Runtime  : 4 ms (beats 100%)
// Memory   : 46272000 (beats 6%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int reversedHalf = 0;
        
        while (x > reversedHalf) {
            reversedHalf = reversedHalf * 10 + x % 10;
            x /= 10;
        }

        return x == reversedHalf || x == reversedHalf / 10;
    }
}