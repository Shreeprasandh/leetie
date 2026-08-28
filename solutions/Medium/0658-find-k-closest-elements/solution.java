// ──────────────────────────────────────────────────
// Problem  : 658. Find K Closest Elements
// Difficulty: Medium
// Tags     : Array, Two Pointers, Binary Search, Sliding Window, Sorting, Heap (Priority Queue)
// Link     : https://leetcode.com/problems/find-k-closest-elements/
// Runtime  : 14 ms (beats 38%)
// Memory   : 47552000 (beats 90%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {

        List<Integer> li = new ArrayList<>();

        for (int i = 0; i < k; i++)
            li.add(arr[i]);

        for (int i = k; i < arr.length; i++) {
            if (Math.abs(arr[i - k] - x) > Math.abs(arr[i] - x)) {
                li.remove(Integer.valueOf(arr[i - k]));
                li.add(arr[i]);
            }
        }

        return li;
    }
}