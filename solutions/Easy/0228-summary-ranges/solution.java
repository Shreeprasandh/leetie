// ──────────────────────────────────────────────────
// Problem  : 228. Summary Ranges
// Difficulty: Easy
// Tags     : Array
// Link     : https://leetcode.com/problems/summary-ranges/
// Runtime  : 3 ms (beats 78%)
// Memory   : 42544000 (beats 98%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public List<String> summaryRanges(int[] arr) {
        List<String> result = new ArrayList<>();
        if(arr.length==0) return result;
        int rangestart = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[i - 1] + 1) {
                if (rangestart == arr[i-1]) {
                    result.add(String.valueOf(rangestart));
                } else {
                    result.add(rangestart + "->" + arr[i - 1]);
                }
                rangestart = arr[i];
            }
        }
        if (rangestart == arr[arr.length - 1]) {
            result.add(String.valueOf(rangestart));
        } else {
            result.add(rangestart + "->" + arr[arr.length - 1]);
        }
        return result;
    }
}