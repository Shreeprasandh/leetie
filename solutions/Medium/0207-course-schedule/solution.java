// ──────────────────────────────────────────────────
// Problem  : 207. Course Schedule
// Difficulty: Medium
// Tags     : Depth-First Search, Breadth-First Search, Graph Theory, Topological Sort, Directed Acyclic Graph
// Link     : https://leetcode.com/problems/course-schedule/
// Runtime  : 7 ms (beats 56%)
// Memory   : 47212000 (beats 33%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }
        
        int[] indegree = new int[numCourses];
        for (int[] p : prerequisites) {
            adj.get(p[1]).add(p[0]);
            indegree[p[0]]++;
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int count = 0;
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            count++;
            
            for (int next : adj.get(curr)) {
                indegree[next]--;
                if (indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return count == numCourses;
    }
}