// ──────────────────────────────────────────────────
// Problem  : 383. Ransom Note
// Difficulty: Easy
// Tags     : Hash Table, String, Counting
// Link     : https://leetcode.com/problems/ransom-note/
// Runtime  : 1 ms (beats 100%)
// Memory   : 46000000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean canConstruct(String ransomNote, String magazine) {
		if (ransomNote.length() > magazine.length()) return false;
        int[] alphabets_counter = new int[26];
        
        for (char c : magazine.toCharArray())
            alphabets_counter[c-'a']++;

        for (char c : ransomNote.toCharArray()){
            if (alphabets_counter[c-'a'] == 0) return false;
            alphabets_counter[c-'a']--;
        }
        return true;
    }
}