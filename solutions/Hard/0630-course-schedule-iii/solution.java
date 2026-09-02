// ──────────────────────────────────────────────────
// Problem  : 630. Course Schedule III
// Difficulty: Hard
// Tags     : Array, Greedy, Sorting, Heap (Priority Queue)
// Link     : https://leetcode.com/problems/course-schedule-iii/
// Runtime  : 32 ms (beats 93%)
// Memory   : 54908000 (beats 81%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int scheduleCourse(int[][] C) {
        Arrays.sort(C, (a,b) -> a[1] - b[1]);
        PriorityQueue<Integer> pq = new PriorityQueue<>((a,b) -> b - a);
        int total = 0;
        for (int[] course : C) {
            int dur = course[0], end = course[1];
            if (dur + total <= end) {
                total += dur;
                pq.add(dur);
            } else if (pq.size() > 0 && pq.peek() > dur) {
                total += dur - pq.poll();
                pq.add(dur);
            }
        }
        return pq.size();
    }
}