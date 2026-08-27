// ──────────────────────────────────────────────────
// Problem  : 557. Reverse Words in a String III
// Difficulty: Easy
// Tags     : Two Pointers, String
// Link     : https://leetcode.com/problems/reverse-words-in-a-string-iii/
// Runtime  : 4 ms (beats 86%)
// Memory   : 46892000 (beats 49%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String reverseWords(String s) {
        // Split the input string into words and reverse each word
        String[] words = s.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            StringBuilder reversedWord = new StringBuilder(word).reverse();
            result.append(reversedWord).append(" ");
        }
        result.deleteCharAt(result.length() - 1);  // Remove the extra space at the end
        
        // Convert StringBuilder to String
        return result.toString();        
    }
}