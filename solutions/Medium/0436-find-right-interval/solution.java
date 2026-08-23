// ──────────────────────────────────────────────────
// Problem  : 436. Find Right Interval
// Difficulty: Medium
// Tags     : Array, Binary Search, Sorting
// Link     : https://leetcode.com/problems/find-right-interval/
// Runtime  : 22 ms (beats 42%)
// Memory   : 50196000 (beats 72%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] findRightInterval(int[][] intervals) {
        int n = intervals.length;
        int[] result = new int[n];
        
        java.util.TreeMap<Integer, Integer> map = new java.util.TreeMap<>();
        for (int i = 0; i < n; i++) {
            map.put(intervals[i][0], i);
        }
        
        for (int i = 0; i < n; i++) {
            int end = intervals[i][1];
            java.util.Map.Entry<Integer, Integer> entry = map.ceilingEntry(end);
            result[i] = (entry == null) ? -1 : entry.getValue();
        }
        
        return result;
    }
}