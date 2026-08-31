// ──────────────────────────────────────────────────
// Problem  : 463. Island Perimeter
// Difficulty: Easy
// Tags     : Array, Depth-First Search, Breadth-First Search, Matrix
// Link     : https://leetcode.com/problems/island-perimeter/
// Runtime  : 4 ms (beats 100%)
// Memory   : 47144000 (beats 68%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int islandPerimeter(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;

        int perimeter = 0;

        // Iterate through all cells in the grid 🗺️
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                // Process only land cells 🏝️
                if (grid[i][j] == 1) {
                    // Check - UP ⬆️
                    if (i == 0 || grid[i - 1][j] == 0) {
                        perimeter++;
                    }
                    // Check - DOWN ⬇️
                    if (i == row - 1 || grid[i + 1][j] == 0) {
                        perimeter++;
                    }
                    // Check - LEFT ⬅️
                    if (j == 0 || grid[i][j - 1] == 0) {
                        perimeter++;
                    }
                    // Check - RIGHT ➡️
                    if (j == col - 1 || grid[i][j + 1] == 0) {
                        perimeter++;
                    }
                }
            }
        }
        return perimeter;
    }
}