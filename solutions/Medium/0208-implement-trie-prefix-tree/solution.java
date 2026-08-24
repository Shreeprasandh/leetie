// ──────────────────────────────────────────────────
// Problem  : 208. Implement Trie (Prefix Tree)
// Difficulty: Medium
// Tags     : Hash Table, String, Design, Trie
// Link     : https://leetcode.com/problems/implement-trie-prefix-tree/
// Runtime  : 47 ms (beats 7%)
// Memory   : 63692000 (beats 6%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }
    
    public void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            curr.children.putIfAbsent(c, new TrieNode());
            curr = curr.children.get(c);
        }
        curr.isEnd = true;
    }
    
    public boolean search(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            curr = curr.children.get(c);
            if (curr == null) {
                return false;
            }
        }
        return curr.isEnd;
    }
    
    public boolean startsWith(String prefix) {
        TrieNode curr = root;
        for (char c : prefix.toCharArray()) {
            curr = curr.children.get(c);
            if (curr == null) {
                return false;
            }
        }
        return true;
    }

    private class TrieNode {
        private java.util.Map<Character, TrieNode> children = new java.util.HashMap<>();
        private boolean isEnd = false;
    }
}