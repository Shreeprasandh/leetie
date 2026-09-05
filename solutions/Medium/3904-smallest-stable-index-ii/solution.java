// ──────────────────────────────────────────────────
// Problem  : 3904. Smallest Stable Index II
// Difficulty: Medium
// Tags     : Array, Prefix Sum
// Link     : https://leetcode.com/problems/smallest-stable-index-ii/
// Runtime  : 2 ms (beats 100%)
// Memory   : 125264000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int firstStableIndex(int[] nums, int k) {
        int n = nums.length;

        int ansIdx = 0;         // index we're currently testing as the answer
        int globalMax = Integer.MIN_VALUE;        // biggest number seen anywhere so far
        int ansMax = Integer.MIN_VALUE; // biggest number up to ansIdx

        for(int i = 0; i < n; i++){
            globalMax = Math.max(globalMax, nums[i]);

            // only update the candidate's max while we're still inside its prefix
            if(i == ansIdx)
                ansMax = Math.max(ansMax, nums[i]);

            // this number is below the allowed floor, jump past it
            if(nums[i] < ansMax - k){
                ansIdx = i + 1;
                ansMax = globalMax;
            }
        }

        return ansIdx < n ? ansIdx : -1;
    }
}