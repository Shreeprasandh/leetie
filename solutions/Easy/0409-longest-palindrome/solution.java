// ──────────────────────────────────────────────────
// Problem  : 409. Longest Palindrome
// Difficulty: Easy
// Tags     : Hash Table, String, Greedy
// Link     : https://leetcode.com/problems/longest-palindrome/
// Runtime  : 9 ms (beats 19%)
// Memory   : 43224000 (beats 42%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int longestPalindrome(String s) {
        int oddCount = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (char ch : s.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0) + 1);
            if (map.get(ch) % 2 == 1)
                oddCount++;
            else
                oddCount--;
        }
        if (oddCount > 1)
            return s.length() - oddCount + 1;
        return s.length();
    }
}