// ──────────────────────────────────────────────────
// Problem  : 421. Maximum XOR of Two Numbers in an Array
// Difficulty: Medium
// Tags     : Array, Hash Table, Bit Manipulation, Trie
// Link     : https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
// Runtime  : 925 ms (beats 36%)
// Memory   : 208796000 (beats 55%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    class TrieNode {
        TrieNode[] children = new TrieNode[2];
    }
    
    private TrieNode root = new TrieNode();
    
    public void insert(int num) {
        TrieNode curr = root;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (curr.children[bit] == null) {
                curr.children[bit] = new TrieNode();
            }
            curr = curr.children[bit];
        }
    }
    
    public int getMax(int num) {
        TrieNode curr = root;
        int maxNum = 0;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int oppositeBit = 1 - bit;
            if (curr.children[oppositeBit] != null) {
                maxNum |= (1 << i);
                curr = curr.children[oppositeBit];
            } else {
                curr = curr.children[bit];
            }
        }
        return maxNum;
    }

    public int findMaximumXOR(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        for (int num : nums) {
            insert(num);
        }
        
        int maxResult = 0;
        for (int num : nums) {
            maxResult = Math.max(maxResult, getMax(num));
        }
        
        return maxResult;
    }
}