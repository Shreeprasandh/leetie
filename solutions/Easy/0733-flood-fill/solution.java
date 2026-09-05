// ──────────────────────────────────────────────────
// Problem  : 733. Flood Fill
// Difficulty: Easy
// Tags     : Array, Depth-First Search, Breadth-First Search, Matrix
// Link     : https://leetcode.com/problems/flood-fill/
// Runtime  : 2 ms (beats 11%)
// Memory   : 46916000 (beats 25%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int n=image.length;
        int m=image[0].length;
        int initial= image[sr][sc];
        boolean[][] visited= new boolean[n][m];
        visited[sr][sc]=true;
        image[sr][sc]=color;
        solve(image, sr, sc, color, n, m, visited, initial);
        return image;
    }
    public void solve(int[][] image, int i, int j, int color, int n, int m, boolean[][] visited, int initial){
        Queue<int[]> q= new LinkedList<>();
        q.add(new int[]{i,j});
        while(!q.isEmpty()){
            int[] u= q.poll();
            int[][] directions= {{1,0},{0,1},{-1,0},{0,-1}};
            for(int[] dir: directions){
                int nr= u[0] + dir[0];
                int nc= u[1] + dir[1];
                if(nr>=0 && nc>=0 && nr<n && nc<m && image[nr][nc]==initial && visited[nr][nc]==false){
                    visited[nr][nc]= true;
                    image[nr][nc]=color;
                    q.add(new int[]{nr,nc});
                }
            }
        }
    }
}