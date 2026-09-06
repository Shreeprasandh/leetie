// ──────────────────────────────────────────────────
// Problem  : 752. Open the Lock
// Difficulty: Medium
// Tags     : Array, Hash Table, String, Breadth-First Search, Bidirectional Search
// Link     : https://leetcode.com/problems/open-the-lock/
// Runtime  : 10 ms (beats 99%)
// Memory   : 46776000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int openLock(String[] deadends, String target) {
        if (target.equals("0000")) return 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        boolean[] seen = new boolean[10000];
        for (String el : deadends)
            seen[Integer.parseInt(el)] = true;
        int targ = Integer.parseInt(target);
        if (seen[0]) return -1;
        for (int turns = 1; !queue.isEmpty(); turns++) {
            int qlen = queue.size();
            for (int i = 0; i < qlen; i++) {
                int curr = queue.poll();
                for (int j = 1; j < 10000; j *= 10) {
                    int mask = curr % (j * 10) / j,
                        masked = curr - (mask * j);
                    for (int k = 1; k < 10; k += 8) {
                        int next = masked + (mask + k) % 10 * j;
                        if (seen[next]) continue;
                        if (next == targ) return turns;
                        seen[next] = true;
                        queue.add(next);
                    }
                }
            }
        }
        return -1;
    }
}