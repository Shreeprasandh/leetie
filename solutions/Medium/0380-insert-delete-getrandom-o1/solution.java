// ──────────────────────────────────────────────────
// Problem  : 380. Insert Delete GetRandom O(1)
// Difficulty: Medium
// Tags     : Array, Hash Table, Math, Design, Randomized
// Link     : https://leetcode.com/problems/insert-delete-getrandom-o1/
// Runtime  : 26 ms (beats 75%)
// Memory   : 100704000 (beats 26%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class RandomizedSet {
    private java.util.List<Integer> list;
    private java.util.Map<Integer, Integer> map;
    private java.util.Random rand;

    public RandomizedSet() {
        this.list = new java.util.ArrayList<>();
        this.map = new java.util.HashMap<>();
        this.rand = new java.util.Random();
    }
    
    public boolean insert(int val) {
        if (map.containsKey(val)) {
            return false;
        }
        map.put(val, list.size());
        list.add(val);
        return true;
    }
    
    public boolean remove(int val) {
        if (!map.containsKey(val)) {
            return false;
        }
        
        int index = map.get(val);
        int lastIndex = list.size() - 1;
        
        if (index != lastIndex) {
            int lastVal = list.get(lastIndex);
            list.set(index, lastVal);
            map.put(lastVal, index);
        }
        
        list.remove(lastIndex);
        map.remove(val);
        return true;
    }
    
    public int getRandom() {
        return list.get(rand.nextInt(list.size()));
    }
}