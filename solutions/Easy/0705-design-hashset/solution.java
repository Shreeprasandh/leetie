// ──────────────────────────────────────────────────
// Problem  : 705. Design HashSet
// Difficulty: Easy
// Tags     : Array, Hash Table, Linked List, Design, Hash Function
// Link     : https://leetcode.com/problems/design-hashset/
// Runtime  : 14 ms (beats 40%)
// Memory   : 49504000 (beats 97%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class MyHashSet {

    class Node {

        int key;
        Node next;

        Node(int key) {
            this.key = key;
        }

    }

    Node[] bucket;

    int size;

    double loadFactor = 0.75;

    public MyHashSet() {
        bucket = new Node[10];
        size = 0;
    }

    private int hash(int key) {
        return key % bucket.length;
    }

    public void add(int key) {

        if(contains(key)) return;

        int index = hash(key);
        Node curr = bucket[index];

        Node newNode = new Node(key);
        newNode.next = curr;
        bucket[index] = newNode;

        size++;
        if((double)size / bucket.length > loadFactor) rehash();

    }

    public void remove(int key) {
        int index = hash(key);

        Node prev = null;
        Node curr = bucket[index];

        while(curr != null) {
            if(curr.key == key) {

                if(prev == null) bucket[index] = curr.next;
                else prev.next = curr.next;

                size--;
                return;

            }

            prev = curr;
            curr = curr.next;

        }
    }

    public boolean contains(int key) {
        int index = hash(key);

        Node curr = bucket[index];

        while(curr != null) {
            if(curr.key == key) return true;
            curr = curr.next;
        }

        return false;
    }

    private void rehash() {
        int index = bucket.length * 2;
        Node[] oldBucket = bucket;

        bucket = new Node[index];

        for(Node head : oldBucket) {
            Node curr = head;
            while(curr != null) {
                Node currNext = curr.next;

                int newIndex = hash(curr.key);
                Node currHead = bucket[newIndex];

                curr.next = currHead;
                bucket[newIndex] = curr;

                curr = currNext;
            }
        }
    }

}