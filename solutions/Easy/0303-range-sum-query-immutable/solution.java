// ──────────────────────────────────────────────────
// Problem  : 303. Range Sum Query - Immutable
// Difficulty: Easy
// Tags     : Array, Design, Prefix Sum
// Link     : https://leetcode.com/problems/range-sum-query-immutable/
// Runtime  : 7 ms (beats 100%)
// Memory   : 47168000 (beats 99%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class NumArray {

    private int[] prefixSum;

    public NumArray(int[] nums) {
        int n = nums.length;
        prefixSum = new int[n + 1];
        
        prefixSum[0] = 0;
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = prefixSum[i - 1] + nums[i - 1];
        }
    }
    
    public int sumRange(int left, int right) {
        return prefixSum[right + 1] - prefixSum[left];
    }
}

/**
 * Your NumArray object will be instantiated and called as such:
 * NumArray obj = new NumArray(nums);
 * int param_1 = obj.sumRange(left,right);
 */