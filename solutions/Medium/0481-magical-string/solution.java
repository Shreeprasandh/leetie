// ──────────────────────────────────────────────────
// Problem  : 481. Magical String
// Difficulty: Medium
// Tags     : Two Pointers, String
// Link     : https://leetcode.com/problems/magical-string/
// Runtime  : 7 ms (beats 43%)
// Memory   : 44068000 (beats 54%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int magicalString(int n) {
        if (n <= 0) return 0;
        if (n <= 3) return 1;

        int[] s = new int[n + 2];
        s[0] = 1;
        s[1] = 2;
        s[2] = 2;

        int countOnes = 1; // already have one '1'
        int i = 2;         // pointer
        int num = 1;       // next number to append
        int index = 3;     // current position

        while (index < n) {
            int repeat = s[i];

            for (int j = 0; j < repeat && index < n; j++) {
                s[index] = num;
                if (num == 1) countOnes++;
                index++;
            }

            // switch between 1 and 2
            num = (num == 1) ? 2 : 1;
            i++;
        }

        return countOnes;
    }
}