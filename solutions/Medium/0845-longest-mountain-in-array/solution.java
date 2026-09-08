// ──────────────────────────────────────────────────
// Problem  : 845. Longest Mountain in Array
// Difficulty: Medium
// Tags     : Array, Two Pointers, Dynamic Programming, Enumeration
// Link     : https://leetcode.com/problems/longest-mountain-in-array/
// Runtime  : 3 ms (beats 74%)
// Memory   : 47176000 (beats 80%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int longestMountain(int[] arr) {
        int n = arr.length;

        int[] left = new int[n];   // increasing run ending at i
        int[] right = new int[n];  // decreasing run starting at i

        for (int i = 0; i < n; i++) {
            left[i] = 1;
            right[i] = 1;
        }

        // climb from the left: extend the run if we're still going up
        for (int i = 1; i < n; i++) {
            if (arr[i] > arr[i - 1]) left[i] += left[i - 1];
        }

        // climb from the right: extend the run if we're still going down
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) right[i] += right[i + 1];
        }

        int maxi = 0;

        // a peak needs BOTH sides (> 1 each); peak counted once, hence -1
        for (int i = 0; i < n; i++) {
            if (left[i] > 1 && right[i] > 1)
                maxi = Math.max(maxi, left[i] + right[i] - 1);
        }

        return maxi;
    }
}