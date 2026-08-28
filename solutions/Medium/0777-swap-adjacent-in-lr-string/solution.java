// ──────────────────────────────────────────────────
// Problem  : 777. Swap Adjacent in LR String
// Difficulty: Medium
// Tags     : Two Pointers, String
// Link     : https://leetcode.com/problems/swap-adjacent-in-lr-string/
// Runtime  : 4 ms (beats 87%)
// Memory   : 44912000 (beats 42%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean canTransform(String start, String result) {
        int n = start.length();
        int i = 0, j = 0;

        while (i < n || j < n) {
            while (i < n && start.charAt(i) == 'X') i++;
            while (j < n && result.charAt(j) == 'X') j++;

            if ((i == n) ^ (j == n)) return false;

            if (i < n && j < n) {
                char c1 = start.charAt(i);
                char c2 = result.charAt(j);

                if (c1 != c2) return false;
                if (c1 == 'L' && j > i) return false;
                if (c1 == 'R' && j < i) return false;
            }

            i++;
            j++;
        }

        return true;
    }
}