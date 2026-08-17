// ──────────────────────────────────────────────────
// Problem  : 103. Binary Tree Zigzag Level Order Traversal
// Difficulty: Medium
// Tags     : N/A
// Link     : https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/
// Runtime  : 0 ms (beats 100%)
// Memory   : 44020000 (beats 10%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        final var arr = new ArrayList<List<Integer>>();
        dfs(arr, root, 0);
        for (var i = 1; i < arr.size(); i += 2) {
            Collections.reverse(arr.get(i));
        }
        return arr;
    }

    public void dfs(final List<List<Integer>> arr, final TreeNode node, final int depth) {
        if (node == null) return;
        if (arr.size() == depth) {
            arr.add(new ArrayList<>());
        }
        final var list = arr.get(depth);
        list.add(node.val);
        dfs(arr, node.left, depth + 1);
        dfs(arr, node.right, depth + 1);
    }
}