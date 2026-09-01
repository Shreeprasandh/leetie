// ──────────────────────────────────────────────────
// Problem  : 525. Contiguous Array
// Difficulty: Medium
// Tags     : Array, Hash Table, Prefix Sum
// Link     : https://leetcode.com/problems/contiguous-array/
// Runtime  : 24 ms (beats 54%)
// Memory   : 65188000 (beats 92%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findMaxLength(int[] nums) {
        HashMap<Integer,Integer> map=new HashMap<>();
        map.put(0,-1);
        int sum=0;
        int maxLen=0;
        for(int i=0;i<nums.length;i++)
        {
            if(nums[i]==0)
            {
                sum--;
            }
            else
            {
                sum++;
            }
            if(map.containsKey(sum))
            {
                maxLen=Math.max(maxLen,i-map.get(sum));
            }
            else
            {
                map.put(sum,i);
            }
        }
        return maxLen;
    }
}