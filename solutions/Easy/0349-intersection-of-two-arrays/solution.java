// ──────────────────────────────────────────────────
// Problem  : 349. Intersection of Two Arrays
// Difficulty: Easy
// Tags     : Array, Hash Table, Two Pointers, Binary Search, Sorting
// Link     : https://leetcode.com/problems/intersection-of-two-arrays/
// Runtime  : 2 ms (beats 97%)
// Memory   : 45080000 (beats 41%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {

        ArrayList<Integer> l1 = new ArrayList<>();
        HashSet<Integer> s1 = new HashSet<>();

        for (int no : nums1) {
            s1.add(no);
        }

        for (int no : nums2) {
            if (s1.contains(no)) {
                s1.remove(no);
                l1.add(no);
            }
        }

        int[] arr = new int[l1.size()];
        int i = 0;

        for (int num : l1) {
            arr[i++] = num;
        }

        return arr;
    }
}