// ──────────────────────────────────────────────────
// Problem  : 1. Two Sum
// Difficulty: Easy
// Tags     : N/A
// Link     : https://leetcode.com/problems/two-sum/
// Runtime  : 39 ms (beats 38%)
// Memory   : 46612000 (beats 94%)
// Language : java
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{}; // No solution found
    }
}