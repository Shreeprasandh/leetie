// ──────────────────────────────────────────────────
// Problem  : 804. Unique Morse Code Words
// Difficulty: Easy
// Tags     : Array, Hash Table, String
// Link     : https://leetcode.com/problems/unique-morse-code-words/
// Runtime  : 2 ms (beats 99%)
// Memory   : 43420000 (beats 43%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int uniqueMorseRepresentations(String[] words) {
        String[] code = {
            ".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--",
            "-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."
        };

        Set<String> set = new HashSet<>();

        for (String word : words) {
            StringBuilder sb = new StringBuilder();
            for (char c : word.toCharArray()) {
                sb.append(code[c - 'a']);
            }
            set.add(sb.toString());
        }

        return set.size();
    }
}