// ──────────────────────────────────────────────────
// Problem  : 2945. Find Maximum Non-decreasing Array Length
// Difficulty: Hard
// Tags     : Array, Binary Search, Dynamic Programming, Stack, Queue, Monotonic Stack, Prefix Sum, Monotonic Queue
// Link     : https://leetcode.com/problems/find-maximum-non-decreasing-array-length/
// Runtime  : 48 ms (beats 50%)
// Memory   : 116788000 (beats 42%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

public class Solution {
    public int findMaximumLength(int[] nums) {
        // Length of the input array
        int n = nums.length;

        // Arrays to store cumulative sums, dynamic programming results, and indices for optimization
        int[] pre = new int[n + 2];
        int[] dp = new int[n + 1];

        // Array to store cumulative sums of the input array
        long[] acc = new long[n + 1];

        // Calculate cumulative sums
        for (int i = 1; i <= n; i++) {
            acc[i] = acc[i - 1] + nums[i - 1];
        }

        // Iterate through the elements of the input array
        for (int i = 0, j = 1; j <= n; j++) {
            // Optimize the starting index i if it was previously optimized
            i = Math.max(i, pre[j]);

            // Update the current dynamic programming result
            dp[j] = dp[i] + 1;

            // Binary search to find an index k where acc[k] is equal to 2 * acc[j] - acc[i]
            int k = Arrays.binarySearch(acc, 2 * acc[j] - acc[i]);

            // If the value is not found, get the insertion point to update the pre array
            if (k < 0) {
                k = -k - 1;
            }

            // Update the pre array with the current index j
            pre[k] = j;
        }

        // Return the maximum length of the subarray
        return dp[n];
    }
}