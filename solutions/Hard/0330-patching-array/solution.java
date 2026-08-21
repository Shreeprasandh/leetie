// ──────────────────────────────────────────────────
// Problem  : 330. Patching Array
// Difficulty: Hard
// Tags     : Array, Greedy
// Link     : https://leetcode.com/problems/patching-array/
// Runtime  : 0 ms (beats 100%)
// Memory   : 45120000 (beats 81%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int minPatches(int[] nums, int n) {
        int patches = 0;
        int i = 0;
        long miss = 1;
        while(miss <= n) {
            if(i < nums.length && nums[i] <= miss)
                miss += nums[i++];
            else {
                miss += miss;
                patches++;
            }
        }
        return patches;
    }
}