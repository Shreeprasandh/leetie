// ──────────────────────────────────────────────────
// Problem  : 438. Find All Anagrams in a String
// Difficulty: Medium
// Tags     : Hash Table, String, Sliding Window
// Link     : https://leetcode.com/problems/find-all-anagrams-in-a-string/
// Runtime  : 10 ms (beats 81%)
// Memory   : 46668000 (beats 84%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();

        if (p.length() > s.length()) return ans;

        int[] pFreq = new int[26];
        int[] window = new int[26];

        for (char c : p.toCharArray()) {
            pFreq[c - 'a']++;
        }

        int k = p.length();

        for (int i = 0; i < k; i++) {
            window[s.charAt(i) - 'a']++;
        }

        if (Arrays.equals(pFreq, window)) {
            ans.add(0);
        }

        for (int i = k; i < s.length(); i++) {
            window[s.charAt(i) - 'a']++;
            window[s.charAt(i - k) - 'a']--;

            if (Arrays.equals(pFreq, window)) {
                ans.add(i - k + 1);
            }
        }

        return ans;
    }
}