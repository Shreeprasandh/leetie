// ──────────────────────────────────────────────────
// Problem  : 350. Intersection of Two Arrays II
// Difficulty: Easy
// Tags     : Array, Hash Table, Two Pointers, Binary Search, Sorting
// Link     : https://leetcode.com/problems/intersection-of-two-arrays-ii/
// Runtime  : 4 ms (beats 50%)
// Memory   : 45312000 (beats 10%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] intersect(int[] nums1, int[] nums2) {

        if (nums1.length > nums2.length)
            return intersect(nums2, nums1);

        Map<Integer, Integer> map = new HashMap<>();

        for (int n : nums1)
            map.merge(n, 1, Integer::sum);

        List<Integer> result = new ArrayList<>();

        for (int num : nums2) {

            int remain = map.getOrDefault(num, 0);

            if (remain > 0) {
                result.add(num);
                map.put(num, remain - 1);
            }
        }

        int[] out = new int[result.size()];

        for (int i = 0; i < out.length; i++)
            out[i] = result.get(i);

        return out;
    }
}