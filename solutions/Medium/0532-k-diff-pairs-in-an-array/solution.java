// ──────────────────────────────────────────────────
// Problem  : 532. K-diff Pairs in an Array
// Difficulty: Medium
// Tags     : Array, Hash Table, Two Pointers, Binary Search, Sorting
// Link     : https://leetcode.com/problems/k-diff-pairs-in-an-array/
// Runtime  : 67 ms (beats 10%)
// Memory   : 46880000 (beats 28%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findPairs(int[] nums, int k) {

        if(k<0) return 0;

        Arrays.sort(nums);

        int count = 0;

        for(int i=0; i<nums.length; i++){
            if(i>0 && nums[i] == nums[i-1]){
                continue;
            }
            for(int j=i+1; j<nums.length; j++){
                if(j > i+1 && nums[j] == nums[j-1]){
                    continue;
                }
                int diff = Math.abs(nums[i] - nums[j]);
                if(diff == k){
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}