// ──────────────────────────────────────────────────
// Problem  : 423. Reconstruct Original Digits from English
// Difficulty: Medium
// Tags     : Hash Table, Math, String
// Link     : https://leetcode.com/problems/reconstruct-original-digits-from-english/
// Runtime  : 5 ms (beats 68%)
// Memory   : 46200000 (beats 87%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String originalDigits(String s) {
        int[] charCount = new int[26];
        for (char c : s.toCharArray()) {
            charCount[c - 'a']++;
        }
        
        int[] digitCount = new int[10];
        
        digitCount[0] = charCount['z' - 'a'];
        digitCount[2] = charCount['w' - 'a'];
        digitCount[4] = charCount['u' - 'a'];
        digitCount[6] = charCount['x' - 'a'];
        digitCount[8] = charCount['g' - 'a'];
        
        digitCount[3] = charCount['h' - 'a'] - digitCount[8];
        digitCount[5] = charCount['f' - 'a'] - digitCount[4];
        digitCount[7] = charCount['s' - 'a'] - digitCount[6];
        
        digitCount[1] = charCount['o' - 'a'] - digitCount[0] - digitCount[2] - digitCount[4];
        digitCount[9] = charCount['i' - 'a'] - digitCount[5] - digitCount[6] - digitCount[8];
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < digitCount[i]; j++) {
                result.append(i);
            }
        }
        
        return result.toString();
    }
}