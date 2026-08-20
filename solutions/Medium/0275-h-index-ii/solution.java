// ──────────────────────────────────────────────────
// Problem  : 275. H-Index II
// Difficulty: Medium
// Tags     : Array, Binary Search
// Link     : https://leetcode.com/problems/h-index-ii/
// Runtime  : 0 ms (beats 100%)
// Memory   : 49780000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int hIndex(int[] citations) 
    {
        int l=0;
        int h=citations.length-1;
        int n=citations.length;
        int ans=0;
        while(l<=h)
        {
            int mid=l+(h-l)/2;
            if(citations[mid]>=n-mid)
            {
                ans=n-mid;
                h=mid-1;
            }
            else
            {
                l=mid+1;
            }
        }
        return ans;
    }
//please upvote;
}