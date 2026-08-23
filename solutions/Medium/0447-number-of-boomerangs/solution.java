// ──────────────────────────────────────────────────
// Problem  : 447. Number of Boomerangs
// Difficulty: Medium
// Tags     : Array, Hash Table, Math
// Link     : https://leetcode.com/problems/number-of-boomerangs/
// Runtime  : 134 ms (beats 59%)
// Memory   : 47144000 (beats 7%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numberOfBoomerangs(int[][] points) {
        int boomerangs = 0;
        
        for (int i = 0; i < points.length; i++) {
            java.util.Map<Integer, Integer> distanceMap = new java.util.HashMap<>();
            
            for (int j = 0; j < points.length; j++) {
                if (i == j) continue;
                
                int dx = points[i][0] - points[j][0];
                int dy = points[i][1] - points[j][1];
                int dist = dx * dx + dy * dy;
                
                distanceMap.put(dist, distanceMap.getOrDefault(dist, 0) + 1);
            }
            
            for (int val : distanceMap.values()) {
                boomerangs += val * (val - 1);
            }
        }
        
        return boomerangs;
    }
}