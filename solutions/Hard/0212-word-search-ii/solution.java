// ──────────────────────────────────────────────────
// Problem  : 212. Word Search II
// Difficulty: Hard
// Tags     : Array, String, Backtracking, Trie, Matrix
// Link     : https://leetcode.com/problems/word-search-ii/
// Runtime  : 184 ms (beats 46%)
// Memory   : 46584000 (beats 95%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {

    char[][] board;
    // 4 directions: down, right, up, left
    int[][] directions = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
    // Global used array to save memory allocation time
    boolean[][] used;
    int m;
    int n;

    public List<String> findWords(char[][] board, String[] words) {
        this.board = board;
        this.m = board.length;
        this.n = board[0].length;
        this.used = new boolean[m][n]; // Initialize once!
        
        Trie root = new Trie();

        // Populate the Trie
        for (String word : words) {
            // Optimization: if word is longer than total board cells, it's impossible to find
            if (word.length() > m * n) continue;
            root.addWord(word);
        }

        List<String> ans = new ArrayList<>();
        
        // Traverse the entire board
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char c = board[i][j];
                int idx = c - 'a';
                
                // If the character matches a starting letter in the Trie, begin Backtracking
                if (root.child[idx] != null) {
                    used[i][j] = true; // Mark as visited
                    backtrack(i, j, root.child[idx], ans, used);
                    used[i][j] = false; // Backtrack: unmark to leave it clean for the next iterations
                }
            }
        }

        return ans;
    }

    void backtrack(int row, int col, Trie trie, List<String> ans, boolean[][] used) {
        
        // Base case: Word found
        if (trie.isWord) {
            trie.isWord = false; // Prevent duplicates without using a HashSet
            ans.add(trie.word);  // Add the pre-stored word directly
        }
        
        // Explore all 4 valid directions
        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];
            
            // Check boundaries and visited status
            if (isValid(nextRow, nextCol) && !used[nextRow][nextCol]) {
                char c = board[nextRow][nextCol];
                int idx = c - 'a';
                
                // Look-ahead: Only traverse further if the prefix exists in the Trie
                if (trie.child[idx] != null) {
                    used[nextRow][nextCol] = true; // Choose
                    
                    backtrack(nextRow, nextCol, trie.child[idx], ans, used); // Explore
                    
                    used[nextRow][nextCol] = false; // Backtrack
                }
            }
        }
    }

    // Helper method to check board boundaries
    boolean isValid(int row, int col) {
        return row >= 0 && row < m && col >= 0 && col < n;
    }

    // Custom Trie implementation
    class Trie {
        Trie[] child = new Trie[26];
        boolean isWord = false;
        String word = null; // Store the word here to avoid StringBuilder overhead

        void addWord(String word) {
            Trie curr = this;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (curr.child[idx] == null) {
                    curr.child[idx] = new Trie();
                }
                curr = curr.child[idx];
            }
            curr.isWord = true;
            curr.word = word; // Save the full string at the leaf node
        }
    }
}