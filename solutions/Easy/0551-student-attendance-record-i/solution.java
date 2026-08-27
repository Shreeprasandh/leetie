// ──────────────────────────────────────────────────
// Problem  : 551. Student Attendance Record I
// Difficulty: Easy
// Tags     : String
// Link     : https://leetcode.com/problems/student-attendance-record-i/
// Runtime  : 0 ms (beats 100%)
// Memory   : 43000000 (beats 42%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean checkRecord(String s) {
        int absentCount = 0;
        int lateCount = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (ch == 'A') {
                absentCount++;
                if (absentCount >= 2) {
                    return false;
                }
            }

            if (ch == 'L') {
                lateCount++;
                if (lateCount >= 3) {
                    return false;
                }
            } else {
                lateCount = 0;
            }
        }

        return true;
    }
}