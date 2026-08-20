// ──────────────────────────────────────────────────
// Problem  : 268. Missing Number
// Difficulty: Easy
// Tags     : Array, Hash Table, Math, Binary Search, Bit Manipulation, Sorting
// Link     : https://leetcode.com/problems/missing-number/
// Runtime  : 0 ms (beats 100%)
// Memory   : 46876000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int missingNumber(int[] nums) {
        int res = nums.length;
        
        for (int i = 0; i < nums.length; i++) {
            res += i - nums[i];
        }
        
        return res;       
    }
}