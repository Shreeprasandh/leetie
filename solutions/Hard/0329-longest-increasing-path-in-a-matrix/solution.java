// ──────────────────────────────────────────────────
// Problem  : 329. Longest Increasing Path in a Matrix
// Difficulty: Hard
// Tags     : Array, Dynamic Programming, Depth-First Search, Breadth-First Search, Graph Theory, Topological Sort, Memoization, Matrix, Directed Acyclic Graph
// Link     : https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
// Runtime  : 0 ms (beats 0%)
// Memory   : 42732000 (beats 0%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int longestIncreasingPath(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m][n];
        for (int d[] : dp) Arrays.fill(d, -1);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dp[i][j] == -1) dfs(matrix, dp, m, n, i, j, -1);
            }
        }
        int max = Integer.MIN_VALUE;
        for (int[] d : dp) {
            for (int i : d) max = Math.max(i, max);
        }
        return max;
    }

    public int dfs(
        int[][] matrix,
        int[][] dp,
        int m,
        int n,
        int i,
        int j,
        int parent
    ) {
        if (
            i >= m || j >= n || i < 0 || j < 0 || matrix[i][j] <= parent
        ) return 0;
        parent = matrix[i][j];
        if (dp[i][j] != -1) return dp[i][j];
        int left = dfs(matrix, dp, m, n, i, j - 1, parent);
        int right = dfs(matrix, dp, m, n, i, j + 1, parent);
        int bottom = dfs(matrix, dp, m, n, i + 1, j, parent);
        int top = dfs(matrix, dp, m, n, i - 1, j, parent);
        dp[i][j] = 1 + Math.max(Math.max(left, right), Math.max(top, bottom));
        return dp[i][j];
    }
}