// ──────────────────────────────────────────────────
// Problem  : 496. Next Greater Element I
// Difficulty: Easy
// Tags     : Array, Hash Table, Stack, Monotonic Stack
// Link     : https://leetcode.com/problems/next-greater-element-i/
// Runtime  : 3 ms (beats 93%)
// Memory   : 44348000 (beats 99%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] ans = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            int j = 0;
            while (j < nums2.length && nums2[j] != nums1[i]) {
                j++;
            }
            j++;
            while (j < nums2.length && nums2[j] <= nums1[i]) {
                j++;
            }
            if (j < nums2.length) {
                ans[i] = nums2[j];
            } else {
                ans[i] = -1;
            }
        }
        return ans;
    }
}