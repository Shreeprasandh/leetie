// ──────────────────────────────────────────────────
// Problem  : 645. Set Mismatch
// Difficulty: Easy
// Tags     : Array, Hash Table, Bit Manipulation, Sorting
// Link     : https://leetcode.com/problems/set-mismatch/
// Runtime  : 14 ms (beats 33%)
// Memory   : 48576000 (beats 7%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] findErrorNums(int[] nums) {

        int n = nums.length;
        int[] ans = new int[2];

        Arrays.sort(nums);

        for (int i = 1; i < n; i++) {
            if (nums[i] == nums[i - 1]) {
                ans[0] = nums[i];
            }
            if (nums[i] - nums[i - 1] > 1) {
                ans[1] = nums[i - 1] + 1;
            }
        }

        if (nums[0] != 1) {
            ans[1] = 1;
        }

        if (nums[n - 1] != n) {
            ans[1] = n;
        }

        return ans;
    }
}