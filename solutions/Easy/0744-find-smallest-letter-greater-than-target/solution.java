// ──────────────────────────────────────────────────
// Problem  : 744. Find Smallest Letter Greater Than Target
// Difficulty: Easy
// Tags     : Array, Binary Search
// Link     : https://leetcode.com/problems/find-smallest-letter-greater-than-target/
// Runtime  : 0 ms (beats 100%)
// Memory   : 46468000 (beats 32%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public char nextGreatestLetter(char[] letters, char target) {
        int left = 0 ;
        int right = letters.length - 1 ;
        char ans = letters[0] ;
        while(left <= right){
            int mid = left + (right - left) / 2 ;

            if(letters[mid] > target){
                ans = letters[mid] ;
                right = mid - 1 ;
            }else{
                left = mid + 1 ;
            }
        }
        return ans ;
    }
}