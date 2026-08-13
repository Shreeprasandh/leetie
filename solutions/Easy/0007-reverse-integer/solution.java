// ──────────────────────────────────────────────────
// Problem  : 7. Reverse Integer
// Difficulty: Easy
// Tags     : N/A
// Link     : https://leetcode.com/problems/reverse-integer/
// Runtime  : 1 ms (beats 100%)
// Memory   : 42664000 (beats 46%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE/10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) return 0;
            if (rev < Integer.MIN_VALUE/10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) return 0;
            rev = rev * 10 + pop;
        }
        return rev;
    }
}