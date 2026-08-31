// ──────────────────────────────────────────────────
// Problem  : 495. Teemo Attacking
// Difficulty: Easy
// Tags     : Array, Simulation
// Link     : https://leetcode.com/problems/teemo-attacking/
// Runtime  : 3 ms (beats 90%)
// Memory   : 48200000 (beats 79%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int total = 0;

        for (int i = 1; i < timeSeries.length; i++) {
            total += Math.min(duration, timeSeries[i] - timeSeries[i - 1]);
        }

        total += duration;

        return total;
    }
}