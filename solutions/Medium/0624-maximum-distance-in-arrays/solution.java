// ──────────────────────────────────────────────────
// Problem  : 624. Maximum Distance in Arrays
// Difficulty: Medium
// Tags     : Array, Greedy
// Link     : https://leetcode.com/problems/maximum-distance-in-arrays/
// Runtime  : 6 ms (beats 98%)
// Memory   : 143188000 (beats 41%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxDistance(List<List<Integer>> arrays) {
        
        int globalMin = arrays.get(0).get(0);
        int globalMax = arrays.get(0).get(arrays.get(0).size() - 1);
        int result = 0;

        for (int i = 1; i < arrays.size(); i++) {
            List<Integer> curr = arrays.get(i);
            int localMin = curr.get(0);
            int localMax = curr.get(curr.size() - 1);

            result = Math.max(result, Math.max(localMax - globalMin, globalMax - localMin));

            globalMin = Math.min(globalMin, localMin);
            globalMax = Math.max(globalMax, localMax);
        }

        return result;
    }
}