// ──────────────────────────────────────────────────
// Problem  : 740. Delete and Earn
// Difficulty: Medium
// Tags     : Array, Hash Table, Dynamic Programming
// Link     : https://leetcode.com/problems/delete-and-earn/
// Runtime  : 8 ms (beats 32%)
// Memory   : 48108000 (beats 5%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int deleteAndEarn(int[] nums) {
        int n = 10001;
        int[]houses = new int[n]; //houses array will act as a house robber problem
        for(int num : nums){
            houses[num] += num;
        }
        return rob(houses);
    }
    
    public int rob(int[] nums) {
        int[]cache = new int[nums.length];
        Arrays.fill(cache, -1);
        return robHelper(nums, 0, cache);
    }
    
    private int robHelper(int[] nums, int idx, int[]cache){
        if(idx >= nums.length) return 0;
        if(cache[idx] != -1) return cache[idx];
        return cache[idx] = Math.max(robHelper(nums,idx+2, cache)+nums[idx], robHelper(nums,idx+1, cache));
    }
}
