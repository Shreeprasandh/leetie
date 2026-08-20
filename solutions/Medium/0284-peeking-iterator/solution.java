// ──────────────────────────────────────────────────
// Problem  : 284. Peeking Iterator
// Difficulty: Medium
// Tags     : Array, Design, Iterator
// Link     : https://leetcode.com/problems/peeking-iterator/
// Runtime  : 4 ms (beats 81%)
// Memory   : 43124000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class PeekingIterator implements Iterator<Integer> {  
    private Integer next = null;
    private Iterator<Integer> iter;

    public PeekingIterator(Iterator<Integer> iterator) {
        // initialize any member here.
        iter = iterator;
        if (iter.hasNext())
            next = iter.next();
    }
    
    // Returns the next element in the iteration without advancing the iterator. 
    public Integer peek() {
        return next; 
    }

    // hasNext() and next() should behave the same as in the Iterator interface.
    // Override them if needed.
    @Override
    public Integer next() {
        Integer res = next;
        next = iter.hasNext() ? iter.next() : null;
        return res; 
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }
}