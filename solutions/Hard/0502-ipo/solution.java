// ──────────────────────────────────────────────────
// Problem  : 502. IPO
// Difficulty: Hard
// Tags     : Array, Greedy, Sorting, Heap (Priority Queue)
// Link     : https://leetcode.com/problems/ipo/
// Runtime  : 118 ms (beats 34%)
// Memory   : 133088000 (beats 68%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        int[][] projects = new int[n][2];
        for (int i = 0; i < n; i++) {
            projects[i][0] = capital[i];
            projects[i][1] = profits[i];
        }
        Arrays.sort(projects, (a, b) -> Integer.compare(a[0], b[0]));
        int i = 0;
        PriorityQueue<Integer> maximizeCapital = new PriorityQueue<>(Collections.reverseOrder());
        while (k-- > 0) {
            while (i < n && projects[i][0] <= w) {
                maximizeCapital.offer(projects[i][1]);
                i++;
            }
            if (maximizeCapital.isEmpty()) {
                break;
            }
            w += maximizeCapital.poll();
        }
        return w;
    }
}