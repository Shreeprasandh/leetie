// ──────────────────────────────────────────────────
// Problem  : 290. Word Pattern
// Difficulty: Easy
// Tags     : Hash Table, String
// Link     : https://leetcode.com/problems/word-pattern/
// Runtime  : 1 ms (beats 84%)
// Memory   : 42648000 (beats 74%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (words.length != pattern.length()) {
            return false;
        }

        java.util.Map<Character, String> charToWord = new java.util.HashMap<>();
        java.util.Map<String, Character> wordToChar = new java.util.HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            String word = words[i];

            if (charToWord.containsKey(c)) {
                if (!charToWord.get(c).equals(word)) {
                    return false;
                }
            } else {
                charToWord.put(c, word);
            }

            if (wordToChar.containsKey(word)) {
                if (!wordToChar.get(word).equals(c)) {
                    return false;
                }
            } else {
                wordToChar.put(word, c);
            }
        }

        return true;
    }
}
