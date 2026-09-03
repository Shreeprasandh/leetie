// ──────────────────────────────────────────────────
// Problem  : 648. Replace Words
// Difficulty: Medium
// Tags     : Array, Hash Table, String, Trie
// Link     : https://leetcode.com/problems/replace-words/
// Runtime  : 13 ms (beats 59%)
// Memory   : 62232000 (beats 59%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {

    class Node {
        Node[] child;
        boolean eow;

        Node() {
            child = new Node[26];
            eow = false;
        }
    }

    Node root = new Node();

    public void insert(String word) {
        Node curr = root;

        for (char ch : word.toCharArray()) {
            int idx = ch - 'a';

            if (curr.child[idx] == null) {
                curr.child[idx] = new Node();
            }

            curr = curr.child[idx];
        }

        curr.eow = true;
    }

    public String change(String word) {
        Node curr = root;
        StringBuilder sb = new StringBuilder();

        for (char ch : word.toCharArray()) {
            int idx = ch - 'a';

            if (curr.child[idx] == null) {
                return word;
            }

            sb.append(ch);
            curr = curr.child[idx];

            if (curr.eow) {
                return sb.toString();
            }
        }

        return word;
    }

    public String replaceWords(List<String> dictionary, String sentence) {

        for (String word : dictionary) {
            insert(word);
        }

        String[] words = sentence.split(" ");

        for (int i = 0; i < words.length; i++) {
            words[i] = change(words[i]);
        }

        return String.join(" ", words);
    }
}