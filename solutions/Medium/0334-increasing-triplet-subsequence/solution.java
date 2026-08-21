// ──────────────────────────────────────────────────
// Problem  : 334. Increasing Triplet Subsequence
// Difficulty: Medium
// Tags     : Array, Greedy, Longest Increasing Subsequence
// Link     : https://leetcode.com/problems/increasing-triplet-subsequence/
// Runtime  : 2 ms (beats 99%)
// Memory   : 122384000 (beats 89%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean increasingTriplet(int[] nums) {
       int min1 = Integer.MAX_VALUE;
       int min2 = Integer.MAX_VALUE;
       for(int n : nums) {
           if(n <= min1) min1 = n;
           else if(n <= min2) min2 = n;
           else return true;
       }
       return false;
    }
}