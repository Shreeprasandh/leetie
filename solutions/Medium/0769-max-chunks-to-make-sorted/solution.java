// ──────────────────────────────────────────────────
// Problem  : 769. Max Chunks To Make Sorted
// Difficulty: Medium
// Tags     : Array, Stack, Greedy, Sorting, Monotonic Stack
// Link     : https://leetcode.com/problems/max-chunks-to-make-sorted/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42356000 (beats 92%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxChunksToSorted(int[] arr) {
        int runningSum = 0, expectedSum = 0, chunks = 0;
        for (int i = 0; i < arr.length; i++) {
            runningSum += arr[i];
            expectedSum += i;
            if (runningSum == expectedSum) {
                chunks++;
            }
        }
        return chunks;
    }
}