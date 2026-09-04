// ──────────────────────────────────────────────────
// Problem  : 718. Maximum Length of Repeated Subarray
// Difficulty: Medium
// Tags     : Array, Binary Search, Dynamic Programming, Sliding Window, Rolling Hash, Hash Function
// Link     : https://leetcode.com/problems/maximum-length-of-repeated-subarray/
// Runtime  : 1480 ms (beats 5%)
// Memory   : 45292000 (beats 93%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findLength(int[] nums1, int[] nums2) {

        int n1 = nums1.length;
        int n2 = nums2.length;
        int maxLen = 0;

        for (int i = 0; i < n1; i++) {

            for (int j = 0; j < n2; j++) {

                int k = 0;

                while ((i + k < n1) &&
                       (j + k < n2) &&
                       nums1[i + k] == nums2[j + k]) {
                    k++;
                }

                maxLen = Math.max(maxLen, k);
            }
        }

        return maxLen;
    }
}