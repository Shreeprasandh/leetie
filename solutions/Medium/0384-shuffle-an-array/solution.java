// ──────────────────────────────────────────────────
// Problem  : 384. Shuffle an Array
// Difficulty: Medium
// Tags     : Array, Math, Design, Randomized
// Link     : https://leetcode.com/problems/shuffle-an-array/
// Runtime  : 49 ms (beats 84%)
// Memory   : 52680000 (beats 9%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    private int[] original;
    private int[] array;
    private java.util.Random rand;

    public Solution(int[] nums) {
        this.original = nums;
        this.array = nums.clone();
        this.rand = new java.util.Random();
    }
    
    public int[] reset() {
        this.array = original.clone();
        return this.array;
    }
    
    public int[] shuffle() {
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }
}