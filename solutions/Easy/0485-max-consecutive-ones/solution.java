// ──────────────────────────────────────────────────
// Problem  : 485. Max Consecutive Ones
// Difficulty: Easy
// Tags     : Array
// Link     : https://leetcode.com/problems/max-consecutive-ones/
// Runtime  : 2 ms (beats 98%)
// Memory   : 52456000 (beats 66%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int maxCnt = 0;
        int curCnt = 0;
        for(int n : nums){
            if(n == 1)
                curCnt++;
            else{
                maxCnt = Math.max(maxCnt, curCnt);
                curCnt = 0;
            }
        }
        return maxCnt>curCnt ? maxCnt : curCnt;
    }
}