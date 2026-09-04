// ──────────────────────────────────────────────────
// Problem  : 3903. Smallest Stable Index I
// Difficulty: Easy
// Tags     : Array, Prefix Sum
// Link     : https://leetcode.com/problems/smallest-stable-index-i/
// Runtime  : 1 ms (beats 100%)
// Memory   : 46376000 (beats 49%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int firstStableIndex(int[] nums, int k) {
        int n = nums.length;
        int[] suffix = new int[n];
 
        int mn = Integer.MAX_VALUE;
        // Build suffix minimum
        for (int i = n - 1; i >= 0; i--) {
            mn = Math.min(mn, nums[i]);
            suffix[i] = mn;
        }
 
        int mx = 0;
        // Find first index with score <= k
        for (int i = 0; i < n; i++) {
            mx = Math.max(mx, nums[i]);
            int score = mx - suffix[i];
            if (score <= k)
                return i;
        }
 
        return -1;
    }
}