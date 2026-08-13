// ──────────────────────────────────────────────────
// Problem  : 189. Rotate Array
// Difficulty: Medium
// Tags     : N/A
// Link     : https://leetcode.com/problems/rotate-array/
// Runtime  : 0 ms (beats 0%)
// Memory   : 42904000 (beats 0%)
// Language : java
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int rotations = k % n;
        int[] copy = Arrays.copyOf(nums, n);

        for (int i = 0; i < n; i++) {
            int j = i >= rotations
                ? i - rotations
                : n - rotations + i;

            nums[i] = copy[j];
        }
    }
}