// ──────────────────────────────────────────────────
// Problem  : 421. Maximum XOR of Two Numbers in an Array
// Difficulty: Medium
// Tags     : Array, Hash Table, Bit Manipulation, Trie
// Link     : https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
// Runtime  : 630 ms (beats 72%)
// Memory   : 241868000 (beats 18%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findMaximumXOR(int[] nums) {
        int max = 0, mask = 0;
        for (int i = 31; i >= 0; i--) {
            mask |= (1 << i);
            java.util.HashSet<Integer> set = new java.util.HashSet<>();
            for (int num : nums) {
                set.add(num & mask);
            }
            int tentative = max | (1 << i);
            for (int prefix : set) {
                if (set.contains(prefix ^ tentative)) {
                    max = tentative;
                    break;
                }
            }
        }
        return max;
    }
}