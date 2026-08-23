// ──────────────────────────────────────────────────
// Problem  : 407. Trapping Rain Water II
// Difficulty: Hard
// Tags     : Array, Breadth-First Search, Heap (Priority Queue), Matrix
// Link     : https://leetcode.com/problems/trapping-rain-water-ii/
// Runtime  : 20 ms (beats 57%)
// Memory   : 47924000 (beats 55%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.PriorityQueue;

class Solution {
    public int trapRainWater(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 || heightMap[0].length == 0) {
            return 0;
        }

        int m = heightMap.length;
        int n = heightMap[0].length;
        boolean[][] visited = new boolean[m][n];
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        // Push all boundary cells into the min-heap
        for (int i = 0; i < m; i++) {
            minHeap.offer(new int[] { heightMap[i][0], i, 0 });
            minHeap.offer(new int[] { heightMap[i][n - 1], i, n - 1 });
            visited[i][0] = true;
            visited[i][n - 1] = true;
        }

        for (int j = 0; j < n; j++) {
            minHeap.offer(new int[] { heightMap[0][j], 0, j });
            minHeap.offer(new int[] { heightMap[m - 1][j], m - 1, j });
            visited[0][j] = true;
            visited[m - 1][j] = true;
        }

        int waterTrapped = 0;
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // Process cells from the lowest boundary inward
        while (!minHeap.isEmpty()) {
            int[] cell = minHeap.poll();
            int height = cell[0];
            int r = cell[1];
            int c = cell[2];

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr >= 0 && nr < m && nc >= 0 && nc < n && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    // If neighbor is lower than the current boundary, it traps water
                    if (heightMap[nr][nc] < height) {
                        waterTrapped += height - heightMap[nr][nc];
                        minHeap.offer(new int[] { height, nr, nc });
                    } else {
                        minHeap.offer(new int[] { heightMap[nr][nc], nr, nc });
                    }
                }
            }
        }

        return waterTrapped;
    }
}