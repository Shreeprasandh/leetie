// ──────────────────────────────────────────────────
// Problem  : 713. Subarray Product Less Than K
// Difficulty: Medium
// Tags     : Array, Binary Search, Sliding Window, Prefix Sum
// Link     : https://leetcode.com/problems/subarray-product-less-than-k/
// Runtime  : 393 ms (beats 7%)
// Memory   : 49152000 (beats 16%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int n = nums.length;
        int ans = 0;

        for (int i = 0; i < n; i++) {
            int product = 1;

            for (int j = i; j < n; j++) {
                product *= nums[j];

                if (product < k) {
                    ans++;
                } else {
                    break;
                }
            }
        }
        return ans;
    }
}