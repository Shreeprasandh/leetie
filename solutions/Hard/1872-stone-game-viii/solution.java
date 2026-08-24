// ──────────────────────────────────────────────────
// Problem  : 1872. Stone Game VIII
// Difficulty: Hard
// Tags     : Array, Math, Dynamic Programming, Minimax, Prefix Sum, Game Theory, Zero-Sum Game
// Link     : https://leetcode.com/problems/stone-game-viii/
// Runtime  : 3 ms (beats 100%)
// Memory   : 92100000 (beats 39%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int stoneGameVIII(int[] stones) {
        int n = stones.length;
        int[] prefixSum = new int[n];
        prefixSum[0] = stones[0];
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + stones[i];
        }
        
        int res = prefixSum[n - 1];
        for (int i = n - 2; i >= 1; i--) {
            res = Math.max(res, prefixSum[i] - res);
        }
        return res;
    }
}