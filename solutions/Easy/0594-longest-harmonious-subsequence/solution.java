// ──────────────────────────────────────────────────
// Problem  : 594. Longest Harmonious Subsequence
// Difficulty: Easy
// Tags     : Array, Hash Table, Sliding Window, Sorting, Counting
// Link     : https://leetcode.com/problems/longest-harmonious-subsequence/
// Runtime  : 18 ms (beats 22%)
// Memory   : 47920000 (beats 83%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.*;

class Solution {
  public int findLHS(int[] nums) {
    Map<Integer, Integer> freq = new HashMap<>();
    for (int num : nums) {
      freq.put(num, freq.getOrDefault(num, 0) + 1);
    }

    int ans = 0;
    for (int key : freq.keySet()) {
      if (freq.containsKey(key + 1)) {
        ans = Math.max(ans, freq.get(key) + freq.get(key + 1));
      }
    }
    return ans;
  }
}