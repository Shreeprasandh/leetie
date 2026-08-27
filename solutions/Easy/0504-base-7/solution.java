// ──────────────────────────────────────────────────
// Problem  : 504. Base 7
// Difficulty: Easy
// Tags     : Math, String
// Link     : https://leetcode.com/problems/base-7/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42996000 (beats 25%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String convertToBase7(int num) {
        return Integer.toString(num, 7); 
    }
}