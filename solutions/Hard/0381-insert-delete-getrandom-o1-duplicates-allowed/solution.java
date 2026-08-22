// ──────────────────────────────────────────────────
// Problem  : 381. Insert Delete GetRandom O(1) - Duplicates allowed
// Difficulty: Hard
// Tags     : Array, Hash Table, Math, Design, Randomized
// Link     : https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/
// Runtime  : 33 ms (beats 33%)
// Memory   : 100020000 (beats 17%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class RandomizedCollection {
    private java.util.List<Integer> list;
    private java.util.Map<Integer, java.util.Set<Integer>> map;
    private java.util.Random rand;

    public RandomizedCollection() {
        this.list = new java.util.ArrayList<>();
        this.map = new java.util.HashMap<>();
        this.rand = new java.util.Random();
    }
    
    public boolean insert(int val) {
        boolean notPresent = !map.containsKey(val);
        if (notPresent) {
            map.put(val, new java.util.LinkedHashSet<>());
        }
        map.get(val).add(list.size());
        list.add(val);
        return notPresent;
    }
    
    public boolean remove(int val) {
        if (!map.containsKey(val) || map.get(val).isEmpty()) {
            return false;
        }
        
        int removeIndex = map.get(val).iterator().next();
        map.get(val).remove(removeIndex);
        
        if (map.get(val).isEmpty()) {
            map.remove(val);
        }
        
        int lastIndex = list.size() - 1;
        if (removeIndex != lastIndex) {
            int lastVal = list.get(lastIndex);
            list.set(removeIndex, lastVal);
            map.get(lastVal).remove(lastIndex);
            map.get(lastVal).add(removeIndex);
        }
        
        list.remove(lastIndex);
        return true;
    }
    
    public int getRandom() {
        return list.get(rand.nextInt(list.size()));
    }
}