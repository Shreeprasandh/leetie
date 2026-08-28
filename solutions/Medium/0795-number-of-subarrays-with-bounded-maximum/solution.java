// ──────────────────────────────────────────────────
// Problem  : 795. Number of Subarrays with Bounded Maximum
// Difficulty: Medium
// Tags     : Array, Two Pointers
// Link     : https://leetcode.com/problems/number-of-subarrays-with-bounded-maximum/
// Runtime  : 3 ms (beats 99%)
// Memory   : 59460000 (beats 75%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numSubarrayBoundedMax(int[] nums, int left, int right) {
        int ans = 0, low = 0, mid = 0;
        for (int num : nums) {
            if (num > right) mid = 0;
            else ans += ++mid;
            if (num >= left) low = 0;
            else ans -= ++low;
        }
        return ans;
    }
}