// ──────────────────────────────────────────────────
// Problem  : 803. Bricks Falling When Hit
// Difficulty: Hard
// Tags     : Array, Union-Find, Matrix
// Link     : https://leetcode.com/problems/bricks-falling-when-hit/
// Runtime  : 16 ms (beats 80%)
// Memory   : 88028000 (beats 54%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    // DSU is always good at joining, not breaking.
    // So, we will firstly create a copy grid, where all hitted cells will be removed...we'll amrk them as 0
    // Now, in this copy grid, we will apply DSU to find the size of the big stable component. 
    // Note that we will connect all the stable components to a virtual TOP node
    // After that, we will start joining the hitted cells in the reversed order.
    // After each join, we will calculate the new size, and answer for that query will be (new - old - 1)
    // -1 since the hitted brick isn't counted

    int m, n;
    int[] parent;
    int[] size;
    static int[] dr = {1, -1, 0, 0};
    static int[] dc = {0, 0, 1, -1};
  
    public int[] hitBricks(int[][] grid, int[][] hits){
        m = grid.length;
        n = grid[0].length;

        int[][] copy = new int[m][n];

        // copy original grid
        for (int i = 0; i < m; i++){
            copy[i] = grid[i].clone();
        }

        // create a TOP virtual node which will connect all the stable components together
        int TOP = m * n;  // because (0 to (n * m - 1)) ids are already assigned to cells of the grid

        // 1 size extra for TOP node
        parent = new int[m * n + 1];
        size = new int[m * n + 1];

        for(int i = 0; i <= m * n; i++){
            parent[i] = i;
            size[i] = 1;
        }
        
        // marking all hits first
        for(int it[] : hits){
            copy[it[0]][it[1]] = 0;
        }

        // build dsu 
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(copy[i][j] == 0){    
                    continue;    // we only want to connect cells have value = 1
                }

                if(i == 0){
                    union(id(i, j), TOP); // connect with TOP node
                }

                if(i > 0 && copy[i - 1][j] == 1){
                    union(id(i, j), id(i - 1, j));  // connecting with top
                }

                if(j > 0 && copy[i][j - 1] == 1){
                    union(id(i, j), id(i, j - 1));  // connecting with left
                }
            }
        }

        int[] res = new int[hits.length];

        // start joining hit cells
        for(int k = hits.length - 1; k >= 0; k--){
            int r = hits[k][0];
            int c = hits[k][1];

            if(grid[r][c] == 0){
                res[k] = 0;
                continue;
            }

            int before = size[find(TOP)];
            copy[r][c] = 1;
            int curr = id(r, c);

            // connect curr to all its neighbors with val = 1, coz we want to connect curr to the TOP component
            if(r == 0){
                union(curr, TOP);
            }

            for(int i = 0; i < 4; i++){
                int nr = r + dr[i];
                int nc = c + dc[i];

                if(nr >= 0 && nr < m && nc >= 0 && nc < n && copy[nr][nc] == 1){
                    union(curr, id(nr, nc));
                }
            }

            int after = size[find(TOP)];
            res[k] = Math.max(0, (after - before - 1)); // to avoid -1
        }
        return res;
    }
    public int id(int i, int j){
        return i * n + j;
    }

    public int find(int x){
        if(parent[x] == x){
            return x;
        }
        return parent[x] = find(parent[x]);
    }

    public void union(int x, int y){
        int px = find(x);
        int py = find(y);

        if(px == py){
            return;
        }
        if(size[py] > size[px]){
            int temp = px;
            px = py;
            py = temp;
        }
        size[px] += size[py];
        parent[py] = px;
        return;
    }
}