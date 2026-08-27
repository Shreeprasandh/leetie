// ──────────────────────────────────────────────────
// Problem  : 539. Minimum Time Difference
// Difficulty: Medium
// Tags     : Array, Math, String, Sorting
// Link     : https://leetcode.com/problems/minimum-time-difference/
// Runtime  : 11 ms (beats 61%)
// Memory   : 48132000 (beats 40%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findMinDifference(List<String> timePoints) {
        int[] minutes = new int[timePoints.size()];
        
        for (int i = 0; i < timePoints.size(); i++) {
            String time = timePoints.get(i);
            minutes[i] = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        }
        
        Arrays.sort(minutes);
        int[] extendedMinutes = Arrays.copyOf(minutes, minutes.length + 1);
        extendedMinutes[extendedMinutes.length - 1] = minutes[0] + 1440;

        int minDiff = Integer.MAX_VALUE;
        for (int i = 1; i < extendedMinutes.length; i++) {
            minDiff = Math.min(minDiff, extendedMinutes[i] - extendedMinutes[i - 1]);
        }

        return minDiff;
    }
}