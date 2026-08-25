// ──────────────────────────────────────────────────
// Problem  : 3718. Smallest Missing Multiple of K
// Difficulty: Easy
// Tags     : Array, Hash Table
// Link     : https://leetcode.com/problems/smallest-missing-multiple-of-k/
// Runtime  : 2 ms (beats 68%)
// Memory   : 45524000 (beats 26%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int missingMultiple(int[] nums, int k) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            seen.add(num);
        }

        int cur = k;
        while (seen.contains(cur)) {
            cur += k;
        }

        return cur;
    }
}