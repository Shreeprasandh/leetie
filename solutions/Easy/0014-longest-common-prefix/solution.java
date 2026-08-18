// ──────────────────────────────────────────────────
// Problem  : 14. Longest Common Prefix
// Difficulty: Easy
// Tags     : Array, String, Trie
// Link     : https://leetcode.com/problems/longest-common-prefix/
// Runtime  : 0 ms (beats 100%)
// Memory   : 43148000 (beats 68%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String longestCommonPrefix(String[] strs) {
        String prefix = strs[0];
        
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) {
                    return "";
                }
            }
        }
        
        return prefix;
    }
}