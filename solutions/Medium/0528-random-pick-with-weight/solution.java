// ──────────────────────────────────────────────────
// Problem  : 528. Random Pick with Weight
// Difficulty: Medium
// Tags     : Array, Math, Binary Search, Prefix Sum, Randomized
// Link     : https://leetcode.com/problems/random-pick-with-weight/
// Runtime  : 28 ms (beats 24%)
// Memory   : 52888000 (beats 15%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    private List<Integer> runningSums;
    private int totalSum;

    public Solution(int[] weights) {

        runningSums = new ArrayList<>();
        int runningSum = 0;

        for (int w : weights) {
            runningSum += w;
            runningSums.add(runningSum);
        }

        totalSum = runningSum;
    }

    public int pickIndex() {
        Random random = new Random();
        int target = random.nextInt(totalSum) + 1;
        int low = 0;
        int high = runningSums.size();

        while (low < high) {
            int mid = low + (high - low) / 2;
            if (target > runningSums.get(mid)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }
}