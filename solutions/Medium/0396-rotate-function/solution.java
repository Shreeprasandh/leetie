// ──────────────────────────────────────────────────
// Problem  : 396. Rotate Function
// Difficulty: Medium
// Tags     : Array, Math, Dynamic Programming
// Link     : https://leetcode.com/problems/rotate-function/
// Runtime  : 4 ms (beats 98%)
// Memory   : 91520000 (beats 6%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxRotateFunction(int[] nums) {
        long sum = 0;
        long f = 0;
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            f += (long) i * nums[i];
        }
        
        long maxF = f;
        for (int i = 1; i < n; i++) {
            f = f + sum - (long) n * nums[n - i];
            maxF = Math.max(maxF, f);
        }
        
        return (int) maxF;
    }
}