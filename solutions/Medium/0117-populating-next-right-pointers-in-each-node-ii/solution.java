// ──────────────────────────────────────────────────
// Problem  : 117. Populating Next Right Pointers in Each Node II
// Difficulty: Medium
// Tags     : N/A
// Link     : https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/
// Runtime  : 1 ms (beats 63%)
// Memory   : 46440000 (beats 19%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

/*
// Definition for a Node.
class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}
    
    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
*/

class Solution {
    public Node connect(Node root) {
        if(root==null)return root;
        Queue<Node>queue=new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()){
            int len=queue.size();
            Node prev=null;
            for(int i=0;i<len;i++){
                Node cur=queue.poll();
                if(prev!=null){
                    prev.next=cur;
                }
                prev=cur;
                if(cur.left!=null)queue.offer(cur.left);
                if(cur.right!=null)queue.offer(cur.right);
            }
            prev.next=null;
        }
        return root;
    }
}