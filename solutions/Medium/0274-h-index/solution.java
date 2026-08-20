// ──────────────────────────────────────────────────
// Problem  : 274. H-Index
// Difficulty: Medium
// Tags     : Array, Sorting, Counting Sort
// Link     : https://leetcode.com/problems/h-index/
// Runtime  : 0 ms (beats 100%)
// Memory   : 43308000 (beats 74%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int hIndex(int[] citations) {
        int papers = citations.length;
        int[] citationBuckets = new int[papers + 1];

        for (int citation : citations) {
            citationBuckets[Math.min(citation, papers)]++;
        }

        int cumulativePapers = 0;
        for (int hIndex = papers; hIndex >= 0; hIndex--) {
            cumulativePapers += citationBuckets[hIndex];
            if (cumulativePapers >= hIndex) {
                return hIndex;
            }
        }
        return 0;        
    }
}