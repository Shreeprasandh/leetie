// ──────────────────────────────────────────────────
// Problem  : 766. Toeplitz Matrix
// Difficulty: Easy
// Tags     : Array, Matrix
// Link     : https://leetcode.com/problems/toeplitz-matrix/
// Runtime  : 0 ms (beats 100%)
// Memory   : 46532000 (beats 26%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean isToeplitzMatrix(int[][] matrix) {
        for(int i=1;i<matrix.length;i++){
            for(int j=1;j<matrix[i].length;j++){
                if(matrix[i][j]!=matrix[i-1][j-1]){
                    return false;
                }
            }
        }
        return true;
    }
}