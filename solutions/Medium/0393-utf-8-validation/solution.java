// ──────────────────────────────────────────────────
// Problem  : 393. UTF-8 Validation
// Difficulty: Medium
// Tags     : Array, Bit Manipulation
// Link     : https://leetcode.com/problems/utf-8-validation/
// Runtime  : 1 ms (beats 100%)
// Memory   : 46636000 (beats 28%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean validUtf8(int[] data) {
        int remainingBytes = 0;
        
        for (int num : data) {
            int byteVal = num & 0xFF;
            
            if (remainingBytes == 0) {
                if ((byteVal >> 7) == 0) {
                    continue;
                } else if ((byteVal >> 5) == 0b110) {
                    remainingBytes = 1;
                } else if ((byteVal >> 4) == 0b1110) {
                    remainingBytes = 2;
                } else if ((byteVal >> 3) == 0b11110) {
                    remainingBytes = 3;
                } else {
                    return false;
                }
            } else {
                if ((byteVal >> 6) != 0b10) {
                    return false;
                }
                remainingBytes--;
            }
        }
        
        return remainingBytes == 0;
    }
}