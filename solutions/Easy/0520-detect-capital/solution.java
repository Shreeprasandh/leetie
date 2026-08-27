// ──────────────────────────────────────────────────
// Problem  : 520. Detect Capital
// Difficulty: Easy
// Tags     : String
// Link     : https://leetcode.com/problems/detect-capital/
// Runtime  : 1 ms (beats 89%)
// Memory   : 43264000 (beats 44%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {

    public boolean detectCapitalUse(String word) {

        int upper = 0;

        for (char ch : word.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                upper++;
            }
        }

        return upper == 0
            || upper == word.length()
            || (upper == 1 && Character.isUpperCase(word.charAt(0)));
    }
}