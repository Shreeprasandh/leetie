// ──────────────────────────────────────────────────
// Problem  : 482. License Key Formatting
// Difficulty: Easy
// Tags     : String
// Link     : https://leetcode.com/problems/license-key-formatting/
// Runtime  : 14 ms (beats 39%)
// Memory   : 46388000 (beats 60%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String licenseKeyFormatting(String s, int k) {
        int count = 0;
        s = s.toUpperCase();
        StringBuilder sb = new StringBuilder();
        for(int i = s.length() - 1; i >= 0; i--){
            char ch = s.charAt(i);
            if(ch == '-'){
                continue;
            }
            if(count == k){
                sb.append('-');
                count = 0;
            }
            sb.append(ch);
            count++; 
        }
        return sb.reverse().toString();
    }
}