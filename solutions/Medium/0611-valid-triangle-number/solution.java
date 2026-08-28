// ──────────────────────────────────────────────────
// Problem  : 611. Valid Triangle Number
// Difficulty: Medium
// Tags     : Array, Two Pointers, Binary Search, Greedy, Sorting
// Link     : https://leetcode.com/problems/valid-triangle-number/
// Runtime  : 28 ms (beats 52%)
// Memory   : 44816000 (beats 100%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.Arrays;

class Solution {
    public int triangleNumber(int[] sides) {
        Arrays.sort(sides);
        int totalTriangles = 0;

        // Fix the largest side
        for (int longest = sides.length - 1; longest >= 2; longest--) {
            int left = 0;
            int right = longest - 1;

            while (left < right) {
                if (sides[left] + sides[right] > sides[longest]) {
                    // all pairs between left..right-1 with right are valid
                    totalTriangles += (right - left);
                    right--;
                } else {
                    left++;
                }
            }
        }
        return totalTriangles;
    }

}