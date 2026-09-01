// ──────────────────────────────────────────────────
// Problem  : 560. Subarray Sum Equals K
// Difficulty: Medium
// Tags     : Array, Hash Table, Prefix Sum
// Link     : https://leetcode.com/problems/subarray-sum-equals-k/
// Runtime  : 26 ms (beats 34%)
// Memory   : 48676000 (beats 71%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int subarraySum(int[] nums, int k) {
       int prefix=0; // Running prefix sum.
        int count=0; // Stores the total number of subarrays
        HashMap <Integer,Integer> map=new HashMap<>();
        map.put(0,1); // Prefix sum 0 exists once before the array starts.
        for(int i=1;i<=nums.length;i++){ 
            prefix+=nums[i-1]; // Calculate the current prefix sum.
            int reqPre=prefix-k; // formulae, currentPrefix - previousPrefix = k
            if(map.containsKey(reqPre)){ // If this required prefix sum appeared before, get its value
                count+=map.get(reqPre);
            }
            map.put(prefix,map.getOrDefault(prefix,0)+1); // Store the current prefix sum for future elements.
            // If it already exists, increase its frequency. Otherwise, start its frequency from 1.
        }
        return count;
    }
}