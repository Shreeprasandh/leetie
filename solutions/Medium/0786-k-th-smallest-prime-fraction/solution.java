// ──────────────────────────────────────────────────
// Problem  : 786. K-th Smallest Prime Fraction
// Difficulty: Medium
// Tags     : Array, Two Pointers, Binary Search, Sorting, Heap (Priority Queue)
// Link     : https://leetcode.com/problems/k-th-smallest-prime-fraction/
// Runtime  : 375 ms (beats 26%)
// Memory   : 85772000 (beats 32%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        
        int n = arr.length;
        
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((x, y) -> Double.compare((double)y[0] / y[1], (double)x[0] / x[1]));
        
        for (int i = 0 ; i < n ; i++) {
            for  (int j = i+1 ; j < n ; j++) {
                maxHeap.add(new int[] {arr[i], arr[j]});
                if (maxHeap.size() > k) maxHeap.poll();
            }
        }
        
        return maxHeap.peek();
    }
}