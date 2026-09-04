// ──────────────────────────────────────────────────
// Problem  : 706. Design HashMap
// Difficulty: Easy
// Tags     : Array, Hash Table, Linked List, Design, Hash Function
// Link     : https://leetcode.com/problems/design-hashmap/
// Runtime  : 33 ms (beats 42%)
// Memory   : 57392000 (beats 53%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class MyHashMap {
    int[] data;
    public MyHashMap() {
        data = new int[1000001];
        Arrays.fill(data, -1);
    }
    public void put(int key, int val) {
        data[key] = val;
    }
    public int get(int key) {
        return data[key];
    }
    public void remove(int key) {
        data[key] = -1;
    }
}