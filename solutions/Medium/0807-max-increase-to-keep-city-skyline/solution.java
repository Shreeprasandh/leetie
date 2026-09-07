// ──────────────────────────────────────────────────
// Problem  : 807. Max Increase to Keep City Skyline
// Difficulty: Medium
// Tags     : Array, Greedy, Matrix
// Link     : https://leetcode.com/problems/max-increase-to-keep-city-skyline/
// Runtime  : 1 ms (beats 53%)
// Memory   : 45412000 (beats 59%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int row[] = new int[n];
        int col[]= new int[m];

        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                row[i]=Math.max(row[i], grid[i][j]);
                col[j]=Math.max(col[j], grid[i][j]);
            }
            
        }

        int sum=0;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                sum+=Math.min(row[i], col[j])-grid[i][j];
            }
        }

        return sum;
    }
}