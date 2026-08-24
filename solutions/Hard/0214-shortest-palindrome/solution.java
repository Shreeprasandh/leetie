// ──────────────────────────────────────────────────
// Problem  : 214. Shortest Palindrome
// Difficulty: Hard
// Tags     : String, Rolling Hash, String Matching, Hash Function, Manacher, Z Algorithm, Knuth–Morris–Pratt Algorithm
// Link     : https://leetcode.com/problems/shortest-palindrome/
// Runtime  : 8 ms (beats 62%)
// Memory   : 47120000 (beats 24%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String shortestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }

        String rev = new StringBuilder(s).reverse().toString();
        String temp = s + "#" + rev;
        
        int[] lps = new int[temp.length()];
        for (int i = 1; i < temp.length(); i++) {
            int j = lps[i - 1];
            while (j > 0 && temp.charAt(i) != temp.charAt(j)) {
                j = lps[j - 1];
            }
            if (temp.charAt(i) == temp.charAt(j)) {
                j++;
            }
            lps[i] = j;
        }

        int len = lps[temp.length() - 1];
        return new StringBuilder(s.substring(len)).reverse().toString() + s;
    }
}