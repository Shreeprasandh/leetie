// ──────────────────────────────────────────────────
// Problem  : 729. My Calendar I
// Difficulty: Medium
// Tags     : Array, Binary Search, Design, Segment Tree, Ordered Set
// Link     : https://leetcode.com/problems/my-calendar-i/
// Runtime  : 22 ms (beats 82%)
// Memory   : 46856000 (beats 94%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class MyCalendar {
    TreeMap<Integer,Integer> calendar = new TreeMap<>();
    public MyCalendar() {
        calendar.put(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    public boolean book(int start, int end) {
        Map.Entry<Integer,Integer> pair = calendar.higherEntry(start);
        boolean res = end <= pair.getValue();
        if (res) calendar.put(end, start);
        return res;
    }
}