// ──────────────────────────────────────────────────
// Problem  : 812. Largest Triangle Area
// Difficulty: Easy
// Tags     : Array, Math, Geometry, Polygons
// Link     : https://leetcode.com/problems/largest-triangle-area/
// Runtime  : 9 ms (beats 62%)
// Memory   : 45732000 (beats 48%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public double largestTriangleArea(int[][] points) {
        double maxArea = 0.0;
        int n = points.length;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = j + 1; k < n; ++k) {
                    int x1 = points[i][0], y1 = points[i][1];
                    int x2 = points[j][0], y2 = points[j][1];
                    int x3 = points[k][0], y3 = points[k][1];
                    double area = 0.5 * Math.abs(x1*(y2 - y3) + x2*(y3 - y1) + x3*(y1 - y2));
                    maxArea = Math.max(maxArea, area);
                }
            }
        }
        return maxArea;
    }
}