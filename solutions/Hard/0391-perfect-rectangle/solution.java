// ──────────────────────────────────────────────────
// Problem  : 391. Perfect Rectangle
// Difficulty: Hard
// Tags     : Array, Hash Table, Math, Geometry, Sweep Line
// Link     : https://leetcode.com/problems/perfect-rectangle/
// Runtime  : 55 ms (beats 5%)
// Memory   : 55856000 (beats 53%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean isRectangleCover(int[][] rectangles) {
        if (rectangles == null || rectangles.length == 0) return false;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        java.util.HashSet<String> set = new java.util.HashSet<>();
        long totalArea = 0;

        for (int[] rect : rectangles) {
            minX = Math.min(minX, rect[0]);
            minY = Math.min(minY, rect[1]);
            maxX = Math.max(maxX, rect[2]);
            maxY = Math.max(maxY, rect[3]);

            totalArea += (long) (rect[2] - rect[0]) * (rect[3] - rect[1]);

            String p1 = rect[0] + " " + rect[1];
            String p2 = rect[0] + " " + rect[3];
            String p3 = rect[2] + " " + rect[1];
            String p4 = rect[2] + " " + rect[3];

            if (!set.add(p1)) set.remove(p1);
            if (!set.add(p2)) set.remove(p2);
            if (!set.add(p3)) set.remove(p3);
            if (!set.add(p4)) set.remove(p4);
        }

        if (!set.contains(minX + " " + minY) ||
            !set.contains(minX + " " + maxY) ||
            !set.contains(maxX + " " + minY) ||
            !set.contains(maxX + " " + maxY) ||
            set.size() != 4) {
            return false;
        }

        long expectedArea = (long) (maxX - minX) * (maxY - minY);
        return totalArea == expectedArea;
    }
}