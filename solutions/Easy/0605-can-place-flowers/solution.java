// ──────────────────────────────────────────────────
// Problem  : 605. Can Place Flowers
// Difficulty: Easy
// Tags     : Array, Greedy
// Link     : https://leetcode.com/problems/can-place-flowers/
// Runtime  : 1 ms (beats 99%)
// Memory   : 47760000 (beats 56%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        for (int i = 0; i < flowerbed.length; i++) {
            boolean left = i == 0 || flowerbed[i - 1] == 0;
            boolean right = i == flowerbed.length - 1 || flowerbed[i + 1] == 0;
            
            if (left && right && flowerbed[i] == 0) {
                flowerbed[i] = 1;
                n--;
            }
        }
        return n <= 0;        
    }
}