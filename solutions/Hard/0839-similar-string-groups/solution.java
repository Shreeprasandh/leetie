// ──────────────────────────────────────────────────
// Problem  : 839. Similar String Groups
// Difficulty: Hard
// Tags     : Array, Hash Table, String, Depth-First Search, Breadth-First Search, Union-Find
// Link     : https://leetcode.com/problems/similar-string-groups/
// Runtime  : 96 ms (beats 35%)
// Memory   : 45536000 (beats 10%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numSimilarGroups(String[] strs) {
        int n = strs.length;
        List<Integer>[] mpp = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            mpp[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (helper(strs[i], strs[j])) {
                    mpp[i].add(j);
                    mpp[j].add(i);
                }
            }
        }

        int c = 0;
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                bfs(i, mpp, visited);
                // dfs(i, mpp, visited);
                c++;
            }
        }

        return c;
    }

    private boolean helper(String a, String b) {
        int d = 0;
        int m = a.length();

        for (int i = 0; i < m; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                d++;
            }
        }

        return d == 0 || d == 2;
    }

    private void dfs(int node, List<Integer>[] mpp, boolean[] visited) {
        visited[node] = true;

        for (int it : mpp[node]) {
            if (!visited[it]) {
                dfs(it, mpp, visited);
            }
        }
    }

    private void bfs(int node, List<Integer>[] mpp, boolean[] visited) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(node);
        visited[node] = true;

        while (!q.isEmpty()) {
            int curr = q.poll();

            for (int it : mpp[curr]) {
                if (!visited[it]) {
                    q.offer(it);
                    visited[it] = true;
                }
            }
        }
    }
}