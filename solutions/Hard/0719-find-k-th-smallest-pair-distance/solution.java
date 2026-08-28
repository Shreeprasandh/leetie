// ──────────────────────────────────────────────────
// Problem  : 719. Find K-th Smallest Pair Distance
// Difficulty: Hard
// Tags     : Array, Two Pointers, Binary Search, Sorting
// Link     : https://leetcode.com/problems/find-k-th-smallest-pair-distance/
// Runtime  : 10 ms (beats 94%)
// Memory   : 47092000 (beats 43%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int smallestDistancePair(int[] numbers, int k) {
        Arrays.sort(numbers);
        int minDistance = 0;
        int maxDistance = numbers[numbers.length - 1] - numbers[0];
        
        while (minDistance < maxDistance) {
            int midDistance = minDistance + (maxDistance - minDistance) / 2;
            int pairsCount = countPairsWithinDistance(numbers, midDistance);
            
            if (pairsCount < k) {
                minDistance = midDistance + 1;
            } else {
                maxDistance = midDistance;
            }
        }
        
        return minDistance;
    }

    private int countPairsWithinDistance(int[] numbers, int targetDistance) {
        int count = 0;
        int left = 0;
        
        for (int right = 1; right < numbers.length; right++) {
            while (numbers[right] - numbers[left] > targetDistance) {
                left++;
            }
            count += right - left;
        }
        
        return count;
    }
}
