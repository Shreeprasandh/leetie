// ──────────────────────────────────────────────────
// Problem  : 434. Number of Segments in a String
// Difficulty: Easy
// Tags     : String
// Link     : https://leetcode.com/problems/number-of-segments-in-a-string/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42928000 (beats 15%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int countSegments(String s) {
        // Split the string into words using spaces as separators
        String[] words = s.split(" ");
        int count = 0;

        // Count non-empty words
        for (String word : words) {
            if (!word.isEmpty()) {
                count++;
            }
        }

        return count;
    }
}