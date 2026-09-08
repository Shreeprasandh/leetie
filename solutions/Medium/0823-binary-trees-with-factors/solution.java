// ──────────────────────────────────────────────────
// Problem  : 823. Binary Trees With Factors
// Difficulty: Medium
// Tags     : Array, Hash Table, Dynamic Programming, Sorting
// Link     : https://leetcode.com/problems/binary-trees-with-factors/
// Runtime  : 23 ms (beats 58%)
// Memory   : 46804000 (beats 56%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int numFactoredBinaryTrees(int[] arr) {
        Arrays.sort(arr);
        Map<Integer, Long> subtreeCount = new HashMap<>();

        for (int root : arr) {
            subtreeCount.put(root, 1L);

            for (int factor : arr) {
                if (factor >= root) {
                    break;
                }

                if (root % factor == 0 && subtreeCount.containsKey(root / factor)) {
                    subtreeCount.put(root, (subtreeCount.get(root) + subtreeCount.get(factor) * subtreeCount.get(root / factor)));
                }
            }
        }

        long totalTrees = 0L;
        for (int key : subtreeCount.keySet()) {
            totalTrees = (totalTrees + subtreeCount.get(key)) % 1_000_000_007;
        }

        return (int) totalTrees;       
    }
}