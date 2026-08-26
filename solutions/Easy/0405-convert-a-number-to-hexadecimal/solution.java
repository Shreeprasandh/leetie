// ──────────────────────────────────────────────────
// Problem  : 405. Convert a Number to Hexadecimal
// Difficulty: Easy
// Tags     : Math, String, Bit Manipulation
// Link     : https://leetcode.com/problems/convert-a-number-to-hexadecimal/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42804000 (beats 11%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String toHex(int num) {

        if (num == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder(8);
        char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        while (num != 0) {

            sb.append(hex[num & 0b1111]);
            num >>>= 4;
        }

        return sb.reverse().toString();
    }
}