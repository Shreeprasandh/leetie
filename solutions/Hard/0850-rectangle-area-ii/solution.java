// ──────────────────────────────────────────────────
// Problem  : 850. Rectangle Area II
// Difficulty: Hard
// Tags     : Array, Segment Tree, Sweep Line, Ordered Set
// Link     : https://leetcode.com/problems/rectangle-area-ii/
// Runtime  : 224 ms (beats 6%)
// Memory   : 56632000 (beats 6%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────


class Solution {
    private List<int[]> hs = new ArrayList<>();
    private Map<String, Long> memo = new HashMap<>();
    private static final int MOD = 1_000_000_007;

    public int rectangleArea(int[][] rectangles) {
        long ans = 0;

        for (int[] rect : rectangles) {
            long curr = area(rect);
            long commonArea = helper(rect, hs.size());
            ans += curr - commonArea;
            hs.add(rect);
        }
        return (int)(ans % MOD);
    }

    private long helper(int[] rect, int idx) {
        String key = Arrays.toString(rect) + "#" + idx;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        long common = 0;
        for (int i = 0; i < idx; i++) {
            int[] inter = intersect(rect, hs.get(i));
            if (inter != null) {
                common += area(inter) - helper(inter, i);
            }
        }

        memo.put(key, common);
        return common;
    }

    private int[] intersect(int[] r1, int[] r2) {
        int x1 = Math.max(r1[0], r2[0]);
        int y1 = Math.max(r1[1], r2[1]);
        int x2 = Math.min(r1[2], r2[2]);
        int y2 = Math.min(r1[3], r2[3]);
        if (x1 < x2 && y1 < y2) {
            return new int[]{x1, y1, x2, y2};
        }
        return null;
    }

    private long area(int[] r) {
        return (long)(r[2] - r[0]) * (r[3] - r[1]);
    }
}