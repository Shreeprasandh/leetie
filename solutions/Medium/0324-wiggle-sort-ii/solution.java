// ──────────────────────────────────────────────────
// Problem  : 324. Wiggle Sort II
// Difficulty: Medium
// Tags     : Array, Divide and Conquer, Greedy, Sorting, Quickselect
// Link     : https://leetcode.com/problems/wiggle-sort-ii/
// Runtime  : 8 ms (beats 97%)
// Memory   : 48668000 (beats 90%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public void wiggleSort(int[] nums) {
        int n=nums.length;
        int[] temp=new int[n];
        for(int i=0;i<n;i++){
            temp[i]=nums[i];
        }
        Arrays.sort(temp);
        int left=(n-1)/2;
        int right=n-1;
        for(int i=0;i<n;i+=2){ 
            nums[i]=temp[left];
            left--;
        }
        for(int i=1;i<n;i+=2){
            nums[i]=temp[right];
            right--;
        }
    }
}