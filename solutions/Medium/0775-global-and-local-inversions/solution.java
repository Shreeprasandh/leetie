// ──────────────────────────────────────────────────
// Problem  : 775. Global and Local Inversions
// Difficulty: Medium
// Tags     : Array, Math
// Link     : https://leetcode.com/problems/global-and-local-inversions/
// Runtime  : 1 ms (beats 91%)
// Memory   : 65628000 (beats 20%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean isIdealPermutation(int[] nums) {
        if(nums.length<3)return true;
        int cnt=0;
        int left = nums[1];
        int prevleft=nums[0];
        for(int j=2;j<nums.length;j++){
            if(prevleft>nums[j])return false;
            prevleft = Math.max(prevleft,left);
            left=nums[j];
        }
        
        return true;
    }
}