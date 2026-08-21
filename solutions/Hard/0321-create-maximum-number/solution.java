// ──────────────────────────────────────────────────
// Problem  : 321. Create Maximum Number
// Difficulty: Hard
// Tags     : Array, Two Pointers, Stack, Greedy, Monotonic Stack
// Link     : https://leetcode.com/problems/create-maximum-number/
// Runtime  : 6 ms (beats 99%)
// Memory   : 46220000 (beats 96%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        int m = nums2.length;
        int[] ans = new int[k];
        
        for (int i = Math.max(0, k - m); i <= Math.min(k, n); i++) {
            int[] candidate = merge(maxArray(nums1, i), maxArray(nums2, k - i), k);
            if (greater(candidate, 0, ans, 0)) {
                ans = candidate;
            }
        }
        return ans;
    }

    private int[] maxArray(int[] nums, int k) {
        int[] res = new int[k];
        for (int i = 0, j = 0; i < nums.length; i++) {
            while (nums.length - i + j > k && j > 0 && res[j - 1] < nums[i]) {
                j--;
            }
            if (j < k) {
                res[j++] = nums[i];
            }
        }
        return res;
    }

    private int[] merge(int[] nums1, int[] nums2, int k) {
        int[] res = new int[k];
        for (int i = 0, j = 0, r = 0; r < k; r++) {
            res[r] = greater(nums1, i, nums2, j) ? nums1[i++] : nums2[j++];
        }
        return res;
    }

    private boolean greater(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
            i++;
            j++;
        }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }
}