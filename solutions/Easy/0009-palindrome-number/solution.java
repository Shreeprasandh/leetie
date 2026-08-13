// ──────────────────────────────────────────────────
// Problem  : 9. Palindrome Number
// Difficulty: Easy
// Tags     : N/A
// Link     : https://leetcode.com/problems/palindrome-number/
// Runtime  : 5 ms (beats 81%)
// Memory   : 46136000 (beats 16%)
// Language : java
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