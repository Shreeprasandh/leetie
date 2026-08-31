// ──────────────────────────────────────────────────
// Problem  : 480. Sliding Window Median
// Difficulty: Hard
// Tags     : Array, Hash Table, Sliding Window, Heap (Priority Queue), Treap
// Link     : https://leetcode.com/problems/sliding-window-median/
// Runtime  : 63 ms (beats 59%)
// Memory   : 73216000 (beats 5%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    PriorityQueue<Integer> left = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
    PriorityQueue<Integer> right = new PriorityQueue<>();
    Map<Integer, Integer> leftMap = new HashMap<>();
    Map<Integer, Integer> rightMap = new HashMap<>();
    int l = 0, r = 0;

    public double[] medianSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        double[] ans = new double[n - k + 1];
        for (int i = 0; i < n; i++) {
            add(nums[i]);
            if (i >= k) remove(nums[i - k]);
            if (i >= k - 1) ans[i - k + 1] = median(k);
        }
        return ans;
    }

    private void add(int x) {
        drainLeft();
        Integer lp = left.isEmpty() ? null : left.peek();
        if (lp == null || x <= lp) {
            left.offer(x);
            leftMap.merge(x, 1, Integer::sum);
            l++;
        } else {
            right.offer(x);
            rightMap.merge(x, 1, Integer::sum);
            r++;
        }
        balance();
    }

    private void balance() {
        if (l > r + 1) {
            drainLeft();
            int x = left.poll();
            leftMap.merge(x, -1, Integer::sum);
            if (leftMap.get(x) == 0) leftMap.remove(x);
            right.offer(x);
            rightMap.merge(x, 1, Integer::sum);
            l--; r++;
        } else if (l < r) {
            drainRight();
            int x = right.poll();
            rightMap.merge(x, -1, Integer::sum);
            if (rightMap.get(x) == 0) rightMap.remove(x);
            left.offer(x);
            leftMap.merge(x, 1, Integer::sum);
            l++; r--;
        }
    }

    private void remove(int x) {
        // IMP :- check which heap owns this x using its own map
        if (leftMap.getOrDefault(x, 0) > 0) {
            leftMap.merge(x, -1, Integer::sum);
            if (leftMap.get(x) == 0) leftMap.remove(x);
            l--;
        } else {
            rightMap.merge(x, -1, Integer::sum);
            if (rightMap.get(x) == 0) rightMap.remove(x);
            r--;
        }
        balance();
    }

    private void drainLeft() {
        while (!left.isEmpty() && leftMap.getOrDefault(left.peek(), 0) == 0)
            left.poll();
    }

    private void drainRight() {
        while (!right.isEmpty() && rightMap.getOrDefault(right.peek(), 0) == 0)
            right.poll();
    }

    private double median(int k) {
        drainLeft(); drainRight();
        if (k % 2 == 1) return left.peek();
        return left.peek() / 2.0 + right.peek() / 2.0;
    }
}