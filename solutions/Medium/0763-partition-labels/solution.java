// ──────────────────────────────────────────────────
// Problem  : 763. Partition Labels
// Difficulty: Medium
// Tags     : Hash Table, Two Pointers, String, Greedy
// Link     : https://leetcode.com/problems/partition-labels/
// Runtime  : 10 ms (beats 24%)
// Memory   : 43780000 (beats 37%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public List<Integer> partitionLabels(String s) {
        Map<Character, Integer> map = new HashMap<>();
        // filling impact of character's
        for(int i = 0; i < s.length(); i++){
            char ch = s.charAt(i);
            map.put(ch, i);
        }
        // making of result
        List<Integer> res = new ArrayList<>();
        int prev = -1;
        int max = 0;
        
        for(int i = 0; i < s.length(); i++){
            char ch = s.charAt(i);
            max = Math.max(max, map.get(ch));
            if(max == i){
                // partition time
                res.add(max - prev);
                prev = max;
            }
        }
        return res;
    }
}