// ──────────────────────────────────────────────────
// Problem  : 215. Kth Largest Element in an Array
// Difficulty: Medium
// Tags     : Array, Divide and Conquer, Sorting, Heap (Priority Queue), Quickselect
// Link     : https://leetcode.com/problems/kth-largest-element-in-an-array/
// Runtime  : 3 ms (beats 100%)
// Memory   : 76504000 (beats 6%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findKthLargest(int[] nums, int k) {
        int offset = 10000;
        int[] count = new int[20001];

        for (int num : nums) {
            count[num + offset]++;
        }

        for (int i = 20000; i >= 0; i--) {
            if (count[i] == 0) {
                continue;
            }

            if (k > count[i]) {
                k -= count[i];
            } else {
                return i - offset;
            }
        }

        return -1;
    }
}