// ──────────────────────────────────────────────────
// Problem  : 1389. Create Target Array in the Given Order
// Difficulty: Easy
// Tags     : Array, Simulation
// Link     : https://leetcode.com/problems/create-target-array-in-the-given-order/
// Runtime  : 0 ms (beats 100%)
// Memory   : 43308000 (beats 64%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] createTargetArray(int[] nums, int[] index) {
        ArrayList<Integer> list=new ArrayList<>();
        for(int i=0; i<nums.length; i++){
            list.add(index[i], nums[i]);
        }
        int target[]=new int[list.size()];
        for(int i=0; i<list.size(); i++){
            target[i]=list.get(i);
        }
        return target;
    }
}