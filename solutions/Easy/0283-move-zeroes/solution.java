// ──────────────────────────────────────────────────
// Problem  : 283. Move Zeroes
// Difficulty: Easy
// Tags     : Array, Two Pointers
// Link     : https://leetcode.com/problems/move-zeroes/
// Runtime  : 2 ms (beats 92%)
// Memory   : 47564000 (beats 89%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public void moveZeroes(int[] nums) {
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] != 0) {
                int temp = nums[right];
                nums[right] = nums[left];
                nums[left] = temp;
                left++;
            }
        }        
    }
}