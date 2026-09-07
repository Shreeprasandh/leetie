// ──────────────────────────────────────────────────
// Problem  : 806. Number of Lines To Write String
// Difficulty: Easy
// Tags     : Array, String
// Link     : https://leetcode.com/problems/number-of-lines-to-write-string/
// Runtime  : 0 ms (beats 100%)
// Memory   : 43120000 (beats 44%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] numberOfLines(int[] widths, String s) {
        char c[]=s.toCharArray();
        int sum=0,l=0,px=0;
        int res[]=new int [2];
        for(char k:c){
            int r=k-'a';
            if(sum+widths[r]>100){
                sum=0;
                l++;
            }
            sum+=widths[r];
        }
        if(sum!=0) res[0]=l+1;
        else res[0]=l;
        res[1]=sum;
        return res;
    }
}