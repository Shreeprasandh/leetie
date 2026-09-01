// ──────────────────────────────────────────────────
// Problem  : 529. Minesweeper
// Difficulty: Medium
// Tags     : Array, Depth-First Search, Breadth-First Search, Matrix
// Link     : https://leetcode.com/problems/minesweeper/
// Runtime  : 0 ms (beats 100%)
// Memory   : 46880000 (beats 77%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
   int[][] dirs = new int[][]{
                               {1,0}, {-1,0}, {0, 1}, {0, -1},
                               {1,1}, {-1,-1}, {1, -1}, {-1, 1}
                               };
   public char[][] updateBoard(char[][] board, int[] click) {
       int r = click[0];
       int c = click[1];

       if(board[r][c] == 'M'){
           board[r][c] = 'X';
       }
       else{
           explore(board, r, c);
       }

       return board;
   }

   void explore(char[][] board, int r, int c){

       if(r<0 || c<0 || r>=board.length || c>=board[0].length || board[r][c] != 'E')
           return;

           int countMines = findMines(board, r, c);

           if(countMines > 0){
               board[r][c] = (char) (countMines + '0');
           }
           else{
               board[r][c] = 'B';

               for(int[] d: dirs){
                   int nR = r + d[0];
                   int nC = c + d[1];

                   explore(board, nR, nC);
               }
           }
   }

   int findMines(char[][] board, int r, int c){

       int count = 0;

        for(int[] d: dirs){
           int nR = r + d[0];
           int nC = c + d[1];

           if(nR >= 0 && nC >=0 && nR<board.length && nC<board[0].length){
               if(board[nR][nC] == 'M')
                   count++;
           }
       }
       return count;
   }
}