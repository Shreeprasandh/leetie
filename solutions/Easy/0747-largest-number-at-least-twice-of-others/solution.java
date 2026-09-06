// ──────────────────────────────────────────────────
// Problem  : 747. Largest Number At Least Twice of Others
// Difficulty: Easy
// Tags     : Array, Sorting
// Link     : https://leetcode.com/problems/largest-number-at-least-twice-of-others/
// Runtime  : 0 ms (beats 100%)
// Memory   : 43168000 (beats 72%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int dominantIndex(int[] nums) {
        int max = Integer.MIN_VALUE;
        int idx = -1;;

        for(int i = 0; i < nums.length; i++){
            if(nums[i] > max){
                max = nums[i];
                idx = i;
            }
        }

        for(int i = 0; i < nums.length; i++){
            if(i != idx && nums[i] * 2 > max){
                return -1;
            }
        }
        return idx;
    }
}