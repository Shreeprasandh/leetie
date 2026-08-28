// ──────────────────────────────────────────────────
// Problem  : 653. Two Sum IV - Input is a BST
// Difficulty: Easy
// Tags     : Hash Table, Two Pointers, Tree, Depth-First Search, Breadth-First Search, Binary Search Tree, Binary Tree
// Link     : https://leetcode.com/problems/two-sum-iv-input-is-a-bst/
// Runtime  : 3 ms (beats 73%)
// Memory   : 47396000 (beats 36%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.HashSet;
import java.util.Set;

/**
 * Given a binary search tree (BST) and a target value 'k', determine if there exist two elements
 * in the BST such that their sum is equal to the given target.
 */
class Solution {
    public boolean findTarget(TreeNode root, int k) {
        // Use a HashSet to efficiently store the values we've seen so far.
        Set<Integer> seen = new HashSet<>();
        return inorderTraversal(root, k, seen);
    }

    private boolean inorderTraversal(TreeNode node, int k, Set<Integer> seen) {
        // Base case: If we reach a null node, there's nothing to check.
        if (node == null) {
            return false;
        }

        // Check if the complement (k - node.val) is already in the 'seen' set.
        // If it is, we've found two numbers that add up to 'k'! 🤩
        if (seen.contains(k - node.val)) {
            return true;
        }

        // Add the current node's value to the 'seen' set.
        seen.add(node.val);

        // Recursively explore the left and right subtrees.
        // The '||' (OR) operator means we return 'true' as soon as we find a pair.
        return inorderTraversal(node.left, k, seen) || inorderTraversal(node.right, k, seen);
    }
}