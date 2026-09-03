// ──────────────────────────────────────────────────
// Problem  : 3876. Construct Uniform Parity Array II
// Difficulty: Medium
// Tags     : Array, Math
// Link     : https://leetcode.com/problems/construct-uniform-parity-array-ii/
// Runtime  : 5 ms (beats 83%)
// Memory   : 121256000 (beats 85%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean uniformArray(int[] a) {
        int mn = Integer.MAX_VALUE;
        int oddCnt = 0;
        for (int x : a) {
            mn = Math.min(mn, x);
            if (x % 2 == 1) oddCnt++;
        }
        // min Element is ODD(remaining even > min) or All Even!
        return mn % 2 != 0 || oddCnt == 0;
    }
}