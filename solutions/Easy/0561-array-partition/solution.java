// ──────────────────────────────────────────────────
// Problem  : 561. Array Partition
// Difficulty: Easy
// Tags     : Array, Greedy, Sorting, Counting Sort
// Link     : https://leetcode.com/problems/array-partition/
// Runtime  : 17 ms (beats 81%)
// Memory   : 49596000 (beats 74%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int result = 0;
        for (int i = 0; i < nums.length; i+=2) {
            result += nums[i];
        }
        return result;
    }
}