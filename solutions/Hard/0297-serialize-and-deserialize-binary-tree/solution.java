// ──────────────────────────────────────────────────
// Problem  : 297. Serialize and Deserialize Binary Tree
// Difficulty: Hard
// Tags     : String, Tree, Depth-First Search, Breadth-First Search, Design, Binary Tree
// Link     : https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
// Runtime  : 10 ms (beats 68%)
// Memory   : 48472000 (beats 27%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

public class Codec {
    
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString();
    }
    
    private void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("null,");
            return;
        }
        sb.append(node.val).append(",");
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] values = data.split(",");
        java.util.Queue<String> queue = new java.util.LinkedList<>(java.util.Arrays.asList(values));
        return deserializeHelper(queue);
    }
    
    private TreeNode deserializeHelper(java.util.Queue<String> queue) {
        String val = queue.poll();
        if (val.equals("null")) {
            return null;
        }
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = deserializeHelper(queue);
        node.right = deserializeHelper(queue);
        return node;
    }
}