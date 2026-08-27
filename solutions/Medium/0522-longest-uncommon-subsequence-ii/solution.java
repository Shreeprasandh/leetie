// ──────────────────────────────────────────────────
// Problem  : 522. Longest Uncommon Subsequence II
// Difficulty: Medium
// Tags     : Array, Hash Table, Two Pointers, String, Sorting
// Link     : https://leetcode.com/problems/longest-uncommon-subsequence-ii/
// Runtime  : 1 ms (beats 100%)
// Memory   : 42924000 (beats 29%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findLUSlength(String[] strs) {
        Arrays.sort(strs, (a, b) -> b.length() - a.length()); // Sort by length descending
        
        for (int i = 0; i < strs.length; i++) {
            boolean isUncommon = true;
            
            for (int j = 0; j < strs.length; j++) {
                if (i != j && isSubsequence(strs[i], strs[j])) {
                    isUncommon = false;
                    break;
                }
            }

            if (isUncommon) {
                return strs[i].length(); // First longest uncommon string
            }
        }

        return -1;
    }

    private static boolean isSubsequence(String a, String b) {
        int i = 0, j = 0;
        while (i < a.length() && j < b.length()) {
            if (a.charAt(i) == b.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == a.length();
    }
}