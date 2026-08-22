// ──────────────────────────────────────────────────
// Problem  : 363. Max Sum of Rectangle No Larger Than K
// Difficulty: Hard
// Tags     : Array, Binary Search, Matrix, Prefix Sum, Ordered Set
// Link     : https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
// Runtime  : 350 ms (beats 59%)
// Memory   : 47056000 (beats 55%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.TreeSet;

class Solution {
    public int maxSumSubmatrix(int[][] matrix, int k) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;

        for (int left = 0; left < cols; left++) {
            int[] rowSum = new int[rows];
            for (int right = left; right < cols; right++) {
                for (int i = 0; i < rows; i++) {
                    rowSum[i] += matrix[i][right];
                }

                TreeSet<Integer> set = new TreeSet<>();
                set.add(0);
                int currentSum = 0;

                for (int sum : rowSum) {
                    currentSum += sum;
                    Integer target = set.ceiling(currentSum - k);
                    if (target != null) {
                        maxSum = Math.max(maxSum, currentSum - target);
                    }
                    set.add(currentSum);
                }
            }
        }
        return maxSum;
    }
}