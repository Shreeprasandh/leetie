// ──────────────────────────────────────────────────
// Problem  : 388. Longest Absolute File Path
// Difficulty: Medium
// Tags     : String, Stack, Depth-First Search
// Link     : https://leetcode.com/problems/longest-absolute-file-path/
// Runtime  : 1 ms (beats 68%)
// Memory   : 42548000 (beats 91%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.HashMap;
import java.util.Map;

class Solution {
    public int lengthLongestPath(String input) {
        String[] lines = input.split("\n");
        Map<Integer, Integer> levelLenMap = new HashMap<>();
        levelLenMap.put(-1, 0);
        int maxLen = 0;

        for (String line : lines) {
            int level = line.lastIndexOf("\t") + 1;
            int len = line.length() - level;
            
            if (line.contains(".")) {
                maxLen = Math.max(maxLen, levelLenMap.get(level - 1) + len);
            } else {
                levelLenMap.put(level, levelLenMap.get(level - 1) + len + 1);
            }
        }

        return maxLen;
    }
}