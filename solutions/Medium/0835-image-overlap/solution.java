// ──────────────────────────────────────────────────
// Problem  : 835. Image Overlap
// Difficulty: Medium
// Tags     : Array, Matrix
// Link     : https://leetcode.com/problems/image-overlap/
// Runtime  : 61 ms (beats 47%)
// Memory   : 46948000 (beats 50%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int largestOverlap(int[][] img1, int[][] img2) {
        int n = img1.length;
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        HashMap<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < n * n; i++) {
            if (img1[i / n][i % n] == 1) {
                l1.add((i / n) * 100 + (i % n));
            }
        }
        for (int i = 0; i < n * n; i++) {
            if (img2[i / n][i % n] == 1) {
                l2.add((i / n) * 100 + (i % n));
            }
        }
        for (int i : l1) {
            for (int j : l2) {
                int diff = i - j;
                count.put(diff, count.getOrDefault(diff, 0) + 1);
            }
        }
        int res = 0;
        for (int value : count.values()) {
            res = Math.max(res, value);
        }

        return res;
    }
}