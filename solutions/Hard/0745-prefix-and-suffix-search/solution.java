// ──────────────────────────────────────────────────
// Problem  : 745. Prefix and Suffix Search
// Difficulty: Hard
// Tags     : Array, Hash Table, String, Design, Trie
// Link     : https://leetcode.com/problems/prefix-and-suffix-search/
// Runtime  : 204 ms (beats 94%)
// Memory   : 136328000 (beats 94%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class WordNode {
    WordNode next;
    String word;
    int index;

    WordNode(String word, int index) {
        this.word = word;
        this.index = index;
    }
}
class TrieNode {
    TrieNode[] children;
    WordNode suffixHead;

    TrieNode() {
        children = new TrieNode[26];
    }
}
class WordFilter {
    private TrieNode root;
    public WordFilter(String[] words) {
        root = new TrieNode();
        for (int i = 0; i < words.length; ++i)
            insertWord(words[i], i);
    }
    private void insertWord(String word, int index) {
        TrieNode trieNode = root;
        for (char c : word.toCharArray()) {
            if (trieNode.children[c - 'a'] == null)
                trieNode.children[c - 'a'] = new TrieNode();

            trieNode = trieNode.children[c - 'a'];
            
            WordNode wordNode = new WordNode(word, index);
            wordNode.next = trieNode.suffixHead;
            trieNode.suffixHead = wordNode;
        }
    }
    public int f(String pref, String suff) {
        TrieNode trieNode = root;
        for (char c : pref.toCharArray()) {
            if (trieNode.children[c - 'a'] == null)
                return -1;

            trieNode = trieNode.children[c - 'a'];
        }
        WordNode wordNode = trieNode.suffixHead;
        while (wordNode != null) {
            if (wordNode.word.endsWith(suff))
                return wordNode.index;

            wordNode = wordNode.next;
        }
        return -1;
    }
}