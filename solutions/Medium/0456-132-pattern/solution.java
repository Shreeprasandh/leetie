// ──────────────────────────────────────────────────
// Problem  : 456. 132 Pattern
// Difficulty: Medium
// Tags     : Array, Binary Search, Stack, Monotonic Stack, Ordered Set
// Link     : https://leetcode.com/problems/132-pattern/
// Runtime  : 40 ms (beats 24%)
// Memory   : 94220000 (beats 94%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean find132pattern(int[] nums) {
        Stack<int[]> st = new Stack<>();
        int cur_min = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int n = nums[i];
            while (!st.isEmpty() && n >= st.peek()[0]) {
                st.pop();
        }

            if (!st.isEmpty() && n > st.peek()[1]) {
                return true;
            }

            st.push(new int[] { n, cur_min });
            cur_min = Math.min(cur_min, n);
        }

        return false;        
    }
}