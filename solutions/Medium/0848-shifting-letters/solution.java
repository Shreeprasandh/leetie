// ──────────────────────────────────────────────────
// Problem  : 848. Shifting Letters
// Difficulty: Medium
// Tags     : Array, String, Prefix Sum
// Link     : https://leetcode.com/problems/shifting-letters/
// Runtime  : 9 ms (beats 42%)
// Memory   : 72152000 (beats 38%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String shiftingLetters(String s, int[] shifts) {
        StringBuilder ans = new StringBuilder(s);
        long shift=0;
        for (int i = s.length()-1; i >=0 ; i--){
            ans.setCharAt(i, (char)((s.charAt(i) - 'a' + (shift+shifts[i]) % 26) % 26 + 'a'));
            shift+=shifts[i];
        }
        return ans.toString();
    }
}