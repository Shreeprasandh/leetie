// ──────────────────────────────────────────────────
// Problem  : 373. Find K Pairs with Smallest Sums
// Difficulty: Medium
// Tags     : Array, Heap (Priority Queue)
// Link     : https://leetcode.com/problems/find-k-pairs-with-smallest-sums/
// Runtime  : 37 ms (beats 40%)
// Memory   : 131356000 (beats 94%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.*;

class Solution {
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums1 == null || nums1.length == 0 || nums2 == null || nums2.length == 0 || k <= 0) {
            return result;
        }

        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> (nums1[a[0]] + nums2[a[1]])));

        for (int i = 0; i < Math.min(nums1.length, k); i++) {
            minHeap.offer(new int[] { i, 0 });
        }

        while (k > 0 && !minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int i = curr[0];
            int j = curr[1];

            result.add(Arrays.asList(nums1[i], nums2[j]));
            k--;

            if (j + 1 < nums2.length) {
                minHeap.offer(new int[] { i, j + 1 });
            }
        }

        return result;
    }
}