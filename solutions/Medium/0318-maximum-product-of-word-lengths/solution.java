// ──────────────────────────────────────────────────
// Problem  : 318. Maximum Product of Word Lengths
// Difficulty: Medium
// Tags     : Array, String, Bit Manipulation
// Link     : https://leetcode.com/problems/maximum-product-of-word-lengths/
// Runtime  : 10 ms (beats 70%)
// Memory   : 49616000 (beats 34%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int maxProduct(String[] words) {
        int n = words.length;
        int[] masks = new int[n];
        
        for (int i=0; i<n; i++)
            for (char c: words[i].toCharArray())
                masks[i] |= (1 << (c - 'a'));
        
        int largest = 0;
        for (int i=0; i<n-1; i++) 
            for (int j=i+1; j<n; j++) 
                if ((masks[i] & masks[j]) == 0) 
					largest = Math.max(largest, words[i].length() * words[j].length());
        
        return largest;
    }
}