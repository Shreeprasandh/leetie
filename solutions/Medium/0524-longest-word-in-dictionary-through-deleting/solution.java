// ──────────────────────────────────────────────────
// Problem  : 524. Longest Word in Dictionary through Deleting
// Difficulty: Medium
// Tags     : Array, Two Pointers, String, Sorting
// Link     : https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
// Runtime  : 9 ms (beats 98%)
// Memory   : 47824000 (beats 27%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String findLongestWord(String S, List<String> D) {
        String ans = "";
        for (String word : D) {
            int a = word.length(), b = ans.length();
            if (a < b || (a == b && word.compareTo(ans) > 0)) continue;
            int pos = -1;
            for (int i = 0; i < a; i++) {
                pos = S.indexOf(word.charAt(i), pos + 1);
                if (pos == -1) break;
            }
            if (pos != -1) ans = word;
        }
        return ans;
    }
}