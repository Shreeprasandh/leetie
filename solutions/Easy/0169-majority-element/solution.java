// ──────────────────────────────────────────────────
// Problem  : 169. Majority Element
// Difficulty: Easy
// Tags     : Array, Hash Table, Divide and Conquer, Sorting, Counting, Boyer–Moore Majority Vote Algorithm
// Link     : https://leetcode.com/problems/majority-element/
// Runtime  : 6 ms (beats 52%)
// Memory   : 55764000 (beats 32%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int majorityElement(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        return nums[n/2];
    }
}