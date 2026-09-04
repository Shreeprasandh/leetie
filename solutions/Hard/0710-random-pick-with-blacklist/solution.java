// ──────────────────────────────────────────────────
// Problem  : 710. Random Pick with Blacklist
// Difficulty: Hard
// Tags     : Array, Hash Table, Math, Binary Search, Sorting, Randomized
// Link     : https://leetcode.com/problems/random-pick-with-blacklist/
// Runtime  : 40 ms (beats 100%)
// Memory   : 68336000 (beats 64%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    private final Random random = new Random();

    private final boolean useBlocked;

    private final int n;
    private final Set<Integer> blocked;

    private final List<Integer> allowed;

    public Solution(int n, int[] blacklist) {
        this.n = n;
        int blSize = blacklist.length;
        useBlocked = (blSize * 2 <= n);
        blocked = new HashSet<>(blSize);
        allowed = new ArrayList<>();
        for (int val : blacklist) {
            blocked.add(val);
        }
        if (!useBlocked) {
            for (int val = 0; val < n; ++val) {
                if (!blocked.contains(val)) {
                    allowed.add(val);
                }
            }
        }
    }
    
    public int pick() {
        if (useBlocked) {
            int val = random.nextInt(0, n);
            while (blocked.contains(val)) {
                val = random.nextInt(0, n);
            }
            return val;
        } else {
            int idx = random.nextInt(0, allowed.size());
            return allowed.get(idx);
        }
    }
}

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(n, blacklist);
 * int param_1 = obj.pick();
 */
 