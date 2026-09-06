// ──────────────────────────────────────────────────
// Problem  : 768. Max Chunks To Make Sorted II
// Difficulty: Hard
// Tags     : Array, Stack, Greedy, Sorting, Monotonic Stack
// Link     : https://leetcode.com/problems/max-chunks-to-make-sorted-ii/
// Runtime  : 0 ms (beats 100%)
// Memory   : 46360000 (beats 66%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxChunksToSorted(int[] arr) {

        int n = arr.length;

        // rMin[i] = minimum element from i ... n-1
        int[] rMin = new int[n];
        rMin[n - 1] = arr[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            rMin[i] = Math.min(rMin[i + 1], arr[i]);
        }

        int leftMax = arr[0];
        int chunks = 0;

        // Try every possible split.
        for (int i = 0; i < n - 1; i++) {
            leftMax = Math.max(leftMax, arr[i]);

            // Every element on the left is <= every element on the right.
            if (leftMax <= rMin[i + 1])
                chunks++;
        }

        // Last chunk.
        return chunks + 1;
    }
}