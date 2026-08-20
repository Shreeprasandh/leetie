// ──────────────────────────────────────────────────
// Problem  : 220. Contains Duplicate III
// Difficulty: Hard
// Tags     : Array, Sliding Window, Sorting, Bucket Sort, Ordered Set
// Link     : https://leetcode.com/problems/contains-duplicate-iii/
// Runtime  : 135 ms (beats 60%)
// Memory   : 112436000 (beats 50%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        if (indexDiff <= 0 || valueDiff < 0) return false;

        TreeSet<Long> window = new TreeSet<>(); // ordered values in current window

        for (int i = 0; i < nums.length; i++) {
            // Remove index that moved out of [i - indexDiff, i - 1].
            if (i > indexDiff) {
                window.remove((long) nums[i - indexDiff - 1]);
            }

            long x = nums[i];
            long low = x - (long) valueDiff;
            long high = x + (long) valueDiff;

            // First value >= low
            Long candidate = window.ceiling(low);

            // ceiling only gives lower side guarantee, so check upper bound too.
            if (candidate != null && candidate <= high) {
                return true;
            }

            window.add(x);
        }

        return false;
    }
}