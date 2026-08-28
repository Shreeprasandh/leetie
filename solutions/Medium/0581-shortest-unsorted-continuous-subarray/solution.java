// ──────────────────────────────────────────────────
// Problem  : 581. Shortest Unsorted Continuous Subarray
// Difficulty: Medium
// Tags     : Array, Two Pointers, Stack, Greedy, Sorting, Monotonic Stack
// Link     : https://leetcode.com/problems/shortest-unsorted-continuous-subarray/
// Runtime  : 0 ms (beats 100%)
// Memory   : 47196000 (beats 71%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findUnsortedSubarray(int[] nums) {
        int minSeen=Integer.MAX_VALUE;
        int maxSeen=Integer.MIN_VALUE;
        int l=-1,r=-1;
        int n = nums.length;
        for(int i=0;i<n;i++){
            if(nums[i]>=maxSeen){
                maxSeen = nums[i];
            }
            else{
                r = i;
            }
        }
        for(int j=n-1;j>=0;j--){
            if(nums[j]<=minSeen){
                minSeen = nums[j];
            }
            else{
                l=j;
            }
        }
        if(r==-1 && l==-1){
            return 0;
        }
        else{
            return r-l+1;
        }
    }
}