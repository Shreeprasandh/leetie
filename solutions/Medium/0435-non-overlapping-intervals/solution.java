// ──────────────────────────────────────────────────
// Problem  : 435. Non-overlapping Intervals
// Difficulty: Medium
// Tags     : Array, Dynamic Programming, Greedy, Sorting
// Link     : https://leetcode.com/problems/non-overlapping-intervals/
// Runtime  : 46 ms (beats 75%)
// Memory   : 115688000 (beats 77%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        int res = 0;

        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        int prev_end = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            if (prev_end > intervals[i][0]) {
                res++;
            } else {
                prev_end = intervals[i][1];
            }
        }

        return res;        
    }
}