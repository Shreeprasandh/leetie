// ──────────────────────────────────────────────────
// Problem  : 720. Longest Word in Dictionary
// Difficulty: Medium
// Tags     : Array, Hash Table, String, Trie, Sorting
// Link     : https://leetcode.com/problems/longest-word-in-dictionary/
// Runtime  : 6 ms (beats 94%)
// Memory   : 47412000 (beats 9%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
	private TreeNode root;
	private String result = "";

	public String longestWord(String[] words) {
		root = new TreeNode();

		for (String w : words)
			insert(w);

		dfs(root);

		return result;
	}

	private void dfs(TreeNode node) {
		if (node == null)
			return;

		if (node.word != null) {
			if (node.word.length() > result.length())
				result = node.word;
			else if (node.word.length() == result.length() && node.word.compareTo(result) < 0)
				result = node.word;
		}

		for (TreeNode child : node.children)
			if (child != null && child.word != null)
				dfs(child);
	}

	private void insert(String word) {
		TreeNode current = root;
		for (char c : word.toCharArray()) {
			if (current.children[c - 'a'] == null)
				current.children[c - 'a'] = new TreeNode();
			current = current.children[c - 'a'];
		}
		current.word = word;
	}

}

class TreeNode {
	TreeNode[] children = new TreeNode[26];
	String word;

	TreeNode () {}
}