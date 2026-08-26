// ──────────────────────────────────────────────────
// Problem  : 449. Serialize and Deserialize BST
// Difficulty: Medium
// Tags     : String, Tree, Depth-First Search, Breadth-First Search, Design, Binary Search Tree, Binary Tree
// Link     : https://leetcode.com/problems/serialize-and-deserialize-bst/
// Runtime  : 4 ms (beats 98%)
// Memory   : 47084000 (beats 54%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

public class Codec {

    private int index = 0;

    // Serialize
    public String serialize(TreeNode root) {

        StringBuilder sb = new StringBuilder();
        preorder(root, sb);

        return sb.toString();
    }

    private void preorder(TreeNode root, StringBuilder sb) {

        if (root == null)
            return;

        sb.append(root.val).append(",");

        preorder(root.left, sb);
        preorder(root.right, sb);
    }

    // Deserialize
    public TreeNode deserialize(String data) {

        if (data.isEmpty())
            return null;

        String[] arr = data.split(",");

        index = 0;

        return build(arr, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private TreeNode build(String[] arr, int min, int max) {

        if (index == arr.length)
            return null;

        int val = Integer.parseInt(arr[index]);

        if (val < min || val > max)
            return null;

        index++;

        TreeNode root = new TreeNode(val);

        root.left = build(arr, min, val);

        root.right = build(arr, val, max);

        return root;
    }
}