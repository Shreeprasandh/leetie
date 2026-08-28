// ──────────────────────────────────────────────────
// Problem  : 633. Sum of Square Numbers
// Difficulty: Medium
// Tags     : Math, Two Pointers, Binary Search
// Link     : https://leetcode.com/problems/sum-of-square-numbers/
// Runtime  : 4 ms (beats 83%)
// Memory   : 42308000 (beats 16%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean judgeSquareSum(int c) {
        long left = 0, right = (long) Math.sqrt(c);
        while (left <= right) {
            if (left * left + right * right == c) return true;
            else if (left * left + right * right > c) right--;
            else left++;
        }
        return false;
    }
}