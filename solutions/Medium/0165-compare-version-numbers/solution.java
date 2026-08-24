// ──────────────────────────────────────────────────
// Problem  : 165. Compare Version Numbers
// Difficulty: Medium
// Tags     : Two Pointers, String
// Link     : https://leetcode.com/problems/compare-version-numbers/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42468000 (beats 97%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int compareVersion(String version1, String version2) {
        int i = 0, j = 0;
        int n1 = version1.length(), n2 = version2.length();
        
        while (i < n1 || j < n2) {
            int num1 = 0;
            while (i < n1 && version1.charAt(i) != '.') {
                num1 = num1 * 10 + (version1.charAt(i) - '0');
                i++;
            }
            
            int num2 = 0;
            while (j < n2 && version2.charAt(j) != '.') {
                num2 = num2 * 10 + (version2.charAt(j) - '0');
                j++;
            }
            
            if (num1 < num2) return -1;
            if (num1 > num2) return 1;
            
            i++;
            j++;
        }
        
        return 0;
    }
}