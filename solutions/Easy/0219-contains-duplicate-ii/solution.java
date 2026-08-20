// ──────────────────────────────────────────────────
// Problem  : 219. Contains Duplicate II
// Difficulty: Easy
// Tags     : Array, Hash Table, Sliding Window
// Link     : https://leetcode.com/problems/contains-duplicate-ii/
// Runtime  : 31 ms (beats 19%)
// Memory   : 125140000 (beats 8%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int val = nums[i];
            if (seen.containsKey(val) && i - seen.get(val) <= k) {
                return true;
            }
            seen.put(val, i);
        }

        return false;        
    }
}