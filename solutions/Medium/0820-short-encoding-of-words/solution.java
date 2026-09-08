// ──────────────────────────────────────────────────
// Problem  : 820. Short Encoding of Words
// Difficulty: Medium
// Tags     : Array, Hash Table, String, Trie
// Link     : https://leetcode.com/problems/short-encoding-of-words/
// Runtime  : 18 ms (beats 51%)
// Memory   : 46904000 (beats 72%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int minimumLengthEncoding(String[] W) {
        Set<String> set = new HashSet<>(Arrays.asList(W));
        for (String word : W)
            if (set.contains(word))
                for (int i = 1; i < word.length(); i++) 
                    set.remove(word.substring(i));
        int ans = set.size();
        for (String word : set) ans += word.length();
        return ans;
    }
}