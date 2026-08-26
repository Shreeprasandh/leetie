// ──────────────────────────────────────────────────
// Problem  : 459. Repeated Substring Pattern
// Difficulty: Easy
// Tags     : String, String Matching, Z Algorithm, Knuth–Morris–Pratt Algorithm
// Link     : https://leetcode.com/problems/repeated-substring-pattern/
// Runtime  : 9 ms (beats 86%)
// Memory   : 46728000 (beats 69%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean repeatedSubstringPattern(String s) {
        int n = s.length();
        for (int i = 1; i <= n / 2; i++) {
            if (n % i == 0 && s.substring(0, i).repeat(n / i).equals(s)) {
                return true;
            }
        }
        return false;
    }
}