// ──────────────────────────────────────────────────
// Problem  : 801. Minimum Swaps To Make Sequences Increasing
// Difficulty: Hard
// Tags     : Array, Dynamic Programming
// Link     : https://leetcode.com/problems/minimum-swaps-to-make-sequences-increasing/
// Runtime  : 3 ms (beats 96%)
// Memory   : 93892000 (beats 54%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int minSwap(int[] A, int[] B) {
        int n = A.length;
        int swap = 1, notSwap = 0;

        for (int i = 1; i < n; i++) {
            int newSwap = Integer.MAX_VALUE;
            int newNotSwap = Integer.MAX_VALUE;

            if (A[i - 1] < A[i] && B[i - 1] < B[i]) {
                newNotSwap = notSwap;      
                newSwap = swap + 1; 
            }
            if (A[i - 1] < B[i] && B[i - 1] < A[i]) {
                newNotSwap = Math.min(newNotSwap, swap);  
                newSwap = Math.min(newSwap, notSwap + 1);      
            }

            swap = newSwap;
            notSwap = newNotSwap;
        }

        return Math.min(swap, notSwap);
    }
}