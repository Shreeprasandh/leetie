// ──────────────────────────────────────────────────
// Problem  : 546. Remove Boxes
// Difficulty: Hard
// Tags     : Array, Dynamic Programming, Memoization
// Link     : https://leetcode.com/problems/remove-boxes/
// Runtime  : 19 ms (beats 100%)
// Memory   : 71620000 (beats 56%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    int[] boxes;
    int[][][] dp;

    public int removeBoxes(int[] boxes) {
        this.boxes = boxes;
        int n = boxes.length;
        dp = new int[n][n][n];
        return solve(0, n - 1, 0);
    }

    private int solve(int l, int r, int k) {
        if (l > r) return 0;

        if (dp[l][r][k] != 0)
            return dp[l][r][k];

        int nl = l;
        int nk = k;

        while (nl + 1 <= r && boxes[nl] == boxes[nl + 1]) {
            nl++;
            nk++;
        }

        int ans = (nk + 1) * (nk + 1) + solve(nl + 1, r, 0);

        for (int i = nl + 1; i <= r; i++) {
            if (boxes[nl] == boxes[i] && boxes[i - 1] != boxes[i]) {
                ans = Math.max(
                    ans,
                    solve(nl + 1, i - 1, 0) + solve(i, r, nk + 1)
                );
            }
        }

        return dp[l][r][k] = ans;
    }
}