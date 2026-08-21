// ──────────────────────────────────────────────────
// Problem  : 327. Count of Range Sum
// Difficulty: Hard
// Tags     : Array, Binary Search, Divide and Conquer, Binary Indexed Tree, Segment Tree, Merge Sort, Ordered Set, Treap
// Link     : https://leetcode.com/problems/count-of-range-sum/
// Runtime  : 61 ms (beats 33%)
// Memory   : 80632000 (beats 31%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; ++i) {
            sums[i + 1] = sums[i] + nums[i];
        }
        return mergeSort(sums, 0, n + 1, lower, upper);
    }

    private int mergeSort(long[] sums, int left, int right, int lower, int upper) {
        if (right - left <= 1) return 0;
        
        int mid = left + (right - left) / 2;
        int count = mergeSort(sums, left, mid, lower, upper) + 
                    mergeSort(sums, mid, right, lower, upper);
        
        int j = mid, k = mid;
        for (int i = left; i < mid; i++) {
            while (k < right && sums[k] - sums[i] < lower) k++;
            while (j < right && sums[j] - sums[i] <= upper) j++;
            count += j - k;
        }
        
        long[] temp = new long[right - left];
        int p1 = left, p2 = mid, p = 0;
        while (p1 < mid || p2 < right) {
            if (p1 < mid && (p2 == right || sums[p1] <= sums[p2])) {
                temp[p++] = sums[p1++];
            } else {
                temp[p++] = sums[p2++];
            }
        }
        System.arraycopy(temp, 0, sums, left, right - left);
        
        return count;
    }
}