// ──────────────────────────────────────────────────
// Problem  : 950. Reveal Cards In Increasing Order
// Difficulty: Medium
// Tags     : Array, Queue, Sorting, Simulation
// Link     : https://leetcode.com/problems/reveal-cards-in-increasing-order/
// Runtime  : 8 ms (beats 23%)
// Memory   : 45708000 (beats 63%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] deckRevealedIncreasing(int[] deck) {
        int n = deck.length;

        // Cards need to be revealed in increasing order
        Arrays.sort(deck);

        // Store all available positions
        Queue<Integer> q = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            q.offer(i);
        }

        int j = 0;
        int[] ans = new int[n];

        while (!q.isEmpty()) {
            // The next smallest card is revealed first,
            // so place it at the first available position.
            int i = q.poll();

            ans[i] = deck[j];
            j++;

            // Simulate moving the next card to the bottom
            if (!q.isEmpty()) {
                i = q.poll();
                q.offer(i);
            }
        }

        return ans;
    }
}