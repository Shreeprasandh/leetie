// ──────────────────────────────────────────────────
// Problem  : 100. Same Tree
// Difficulty: Easy
// Tags     : N/A
// Link     : https://leetcode.com/problems/same-tree/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42048000 (beats 100%)
// Language : java
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}