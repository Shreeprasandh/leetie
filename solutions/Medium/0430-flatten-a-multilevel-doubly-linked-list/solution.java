// ──────────────────────────────────────────────────
// Problem  : 430. Flatten a Multilevel Doubly Linked List
// Difficulty: Medium
// Tags     : Linked List, Depth-First Search, Doubly-Linked List
// Link     : https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/
// Runtime  : 0 ms (beats 100%)
// Memory   : 43176000 (beats 94%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public Node flatten(Node head) {
        if(head == null) return null;
        Node curr = head;
        while(curr != null) {
            if(curr.child != null) {
                Node nextNode = curr.next;
                Node childTail = curr.child;
                while(childTail.next != null) childTail = childTail.next;
                if(nextNode != null) {
                    childTail.next = nextNode;
                    nextNode.prev = childTail;
                }
                curr.next = curr.child;
                curr.child.prev = curr;
                curr.child = null;
            }
            curr = curr.next;
        }    
        return head;
    }
}