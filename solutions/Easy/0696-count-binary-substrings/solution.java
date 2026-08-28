// ──────────────────────────────────────────────────
// Problem  : 696. Count Binary Substrings
// Difficulty: Easy
// Tags     : Two Pointers, String
// Link     : https://leetcode.com/problems/count-binary-substrings/
// Runtime  : 11 ms (beats 50%)
// Memory   : 46216000 (beats 70%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int countBinarySubstrings(String s) {
        int prev = 0, curr = 1, count = 0;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                curr++;
            } else {
                count += Math.min(prev, curr);
                prev = curr;
                curr = 1;
            }
        }

        count += Math.min(prev, curr);
        return count;
    }
}
