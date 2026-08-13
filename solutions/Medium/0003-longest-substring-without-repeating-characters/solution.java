// ──────────────────────────────────────────────────
// Problem  : 3. Longest Substring Without Repeating Characters
// Difficulty: Medium
// Tags     : N/A
// Link     : https://leetcode.com/problems/longest-substring-without-repeating-characters/
// Runtime  : 65 ms (beats 19%)
// Memory   : 47972000 (beats 11%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int n = s.length();
        int left = 0;
        int maxL = 0;
        for(int right = 0; right < n; right++){
            while(set.contains(s.charAt(right))){
                set.remove(s.charAt(left));
                left++;
            }
            set.add(s.charAt(right));
            maxL = Math.max(maxL , right - left + 1);
        }
        return maxL;
    }
}