// ──────────────────────────────────────────────────
// Problem  : 646. Maximum Length of Pair Chain
// Difficulty: Medium
// Tags     : Array, Dynamic Programming, Greedy, Sorting, Longest Increasing Subsequence
// Link     : https://leetcode.com/problems/maximum-length-of-pair-chain/
// Runtime  : 12 ms (beats 45%)
// Memory   : 47344000 (beats 15%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findLongestChain(int[][] pairs) {
        Arrays.sort(pairs, Comparator.comparingInt(a -> a[1]));

        int[] prev = pairs[0];
        int res = 1;

        for (int i = 1; i < pairs.length; i++) {
            int[] cur = pairs[i];
            if (cur[0] > prev[1]) {
                res++;
                prev = cur;
            }
        }

        return res;        
    }
}