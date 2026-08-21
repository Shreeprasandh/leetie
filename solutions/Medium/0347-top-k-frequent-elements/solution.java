// ──────────────────────────────────────────────────
// Problem  : 347. Top K Frequent Elements
// Difficulty: Medium
// Tags     : Array, Hash Table, Divide and Conquer, Sorting, Heap (Priority Queue), Bucket Sort, Counting, Quickselect
// Link     : https://leetcode.com/problems/top-k-frequent-elements/
// Runtime  : 16 ms (beats 38%)
// Memory   : 48020000 (beats 11%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] topKFrequent(int[] nums, int k) {

        // Step 1: Count frequency of every number
        HashMap<Integer,Integer> map = new HashMap<>();

        for(int element : nums){
            map.put(element, map.getOrDefault(element,0)+1);
        }

        // Step 2: Convert HashMap into a List
        ArrayList<Map.Entry<Integer,Integer>> arr =
                new ArrayList<>(map.entrySet());

        // Step 3: Sort by frequency in descending order
        arr.sort((a,b)->b.getValue()-a.getValue());

        // Step 4: Pick first K elements
        int[] last = new int[k];

        for(int i=0;i<k;i++){
            last[i]=arr.get(i).getKey();
        }

        return last;
    }
}