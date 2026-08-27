// ──────────────────────────────────────────────────
// Problem  : 599. Minimum Index Sum of Two Lists
// Difficulty: Easy
// Tags     : Array, Hash Table, String
// Link     : https://leetcode.com/problems/minimum-index-sum-of-two-lists/
// Runtime  : 8 ms (beats 97%)
// Memory   : 47708000 (beats 50%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String[] findRestaurant(String[] list1, String[] list2) {
        HashMap<String, Integer> map = new HashMap<>();
        for(int i = 0; i < list1.length; i++){
            map.put(list1[i], i);
        }
        ArrayList<String> list = new ArrayList<>();
        int minSum = Integer.MAX_VALUE;
        for(int j = 0; j < list2.length; j++){
            if(map.containsKey(list2[j])){
                int indexSum = map.get(list2[j]) + j;
                if(indexSum< minSum){
                    minSum = indexSum;
                    list.clear();
                    list.add(list2[j]);
                }
                else if(indexSum == minSum){
                    list.add(list2[j]);
                }
            }
        }
        return list.toArray(new String[0]);
    }
}