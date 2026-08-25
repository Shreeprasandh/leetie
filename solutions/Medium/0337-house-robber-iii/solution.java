// ──────────────────────────────────────────────────
// Problem  : 337. House Robber III
// Difficulty: Medium
// Tags     : Dynamic Programming, Tree, Depth-First Search, Binary Tree, DP on Trees
// Link     : https://leetcode.com/problems/house-robber-iii/
// Runtime  : 2 ms (beats 28%)
// Memory   : 46976000 (beats 13%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
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
    private int rec(TreeNode root,HashMap<TreeNode,Integer> dp){
        if(root==null){
            return 0;
        }
        if(dp.containsKey(root)==true){
            return dp.get(root);
        }
        int notTake = rec(root.left,dp)+rec(root.right,dp);
        int take = root.val;
        if(root.left!=null){
            take+=rec(root.left.left,dp)+rec(root.left.right,dp);
        }
        if(root.right!=null){
            take+=rec(root.right.right,dp)+rec(root.right.left,dp);
        }
        int ans = Math.max(take,notTake);
        dp.put(root,ans);
        return ans;
    }
    public int rob(TreeNode root) {
        HashMap<TreeNode,Integer> dp = new HashMap<>();
        return  rec(root,dp);
    }
}