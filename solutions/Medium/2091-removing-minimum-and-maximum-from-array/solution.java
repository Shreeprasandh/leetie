// ──────────────────────────────────────────────────
// Problem  : 2091. Removing Minimum and Maximum From Array
// Difficulty: Medium
// Tags     : Array, Greedy
// Link     : https://leetcode.com/problems/removing-minimum-and-maximum-from-array/
// Runtime  : 3 ms (beats 62%)
// Memory   : 86580000 (beats 80%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int minimumDeletions(int[] nums) {
        int n = nums.length;

        int minIndex = 0, maxIndex = 0;

        for(int i = 0; i < n; i++){
            if(nums[i] < nums[minIndex]) minIndex = i;
            if(nums[i] > nums[maxIndex]) maxIndex = i;
        }

        int left = Math.min(minIndex, maxIndex);
        int right = Math.max(minIndex, maxIndex);

        // Remove both from the front
        int front = right + 1;

        // Remove both from the back
        int back = n - left;

        // Remove one from each side
        int frontBack = (left + 1) + (n - right);

        return Math.min(front, Math.min(back, frontBack));
    }
}