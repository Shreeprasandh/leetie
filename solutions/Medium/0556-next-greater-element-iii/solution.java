// ──────────────────────────────────────────────────
// Problem  : 556. Next Greater Element III
// Difficulty: Medium
// Tags     : Math, Two Pointers, String
// Link     : https://leetcode.com/problems/next-greater-element-iii/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42304000 (beats 9%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int nextGreaterElement(int n) {

        char[] arr = String.valueOf(n).toCharArray();

        int i = arr.length - 2;
        while (i >= 0 && arr[i] >= arr[i + 1]) { i--; }

        if (i < 0) return -1;

        int j = arr.length - 1;
        while (arr[j] <= arr[i]) { j--; }

        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

        for (int l = i + 1, r = arr.length - 1; l < r; l++, r--) 
        {
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
        }

        long ans = Long.parseLong(new String(arr));

        return ans > Integer.MAX_VALUE ? -1 : (int) ans;
    }
}