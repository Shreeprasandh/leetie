// ──────────────────────────────────────────────────
// Problem  : 382. Linked List Random Node
// Difficulty: Medium
// Tags     : Linked List, Math, Reservoir Sampling, Randomized
// Link     : https://leetcode.com/problems/linked-list-random-node/
// Runtime  : 11 ms (beats 87%)
// Memory   : 47704000 (beats 92%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    int N = 0;
    ListNode head = null;
    public Solution(ListNode head) {
        this.head = head;
    }
    
    public int getRandom() {
        ListNode p = this.head;
        int i = 1, ans = 0;
        while (p != null) {
            if (Math.random() * i < 1) ans = p.val; // replace ans with i-th node.val with probability 1/i
            p = p.next;
            i ++;
        }
        return ans;
    }
}