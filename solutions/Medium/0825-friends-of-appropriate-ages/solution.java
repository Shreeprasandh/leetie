// ──────────────────────────────────────────────────
// Problem  : 825. Friends Of Appropriate Ages
// Difficulty: Medium
// Tags     : Array, Two Pointers, Binary Search, Sorting
// Link     : https://leetcode.com/problems/friends-of-appropriate-ages/
// Runtime  : 1263 ms (beats 14%)
// Memory   : 48920000 (beats 18%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numFriendRequests(int[] ages) {
        int n = ages.length;
        Arrays.sort(ages);
        int count=0;
        for(int i=n-1;i>=0;i--)
            {
                for(int j=i-1;j>=0;j--)
                    {
                        if(ages[j]> (0.5*ages[i]+7))
                        {
                            count++;
                            if(ages[i]==ages[j])count++;
                        }
                        else break;
                    }
            }
        return count;
    }
}