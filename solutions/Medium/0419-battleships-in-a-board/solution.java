// ──────────────────────────────────────────────────
// Problem  : 419. Battleships in a Board
// Difficulty: Medium
// Tags     : Array, Depth-First Search, Matrix
// Link     : https://leetcode.com/problems/battleships-in-a-board/
// Runtime  : 1 ms (beats 99%)
// Memory   : 45220000 (beats 92%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int countBattleships(char[][] board) {
        int count = 0;
        int m = board.length;
        int n = board[0].length;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'X') {
                    if (i > 0 && board[i - 1][j] == 'X') continue;
                    if (j > 0 && board[i][j - 1] == 'X') continue;
                    count++;
                }
            }
        }
        
        return count;
    }
}