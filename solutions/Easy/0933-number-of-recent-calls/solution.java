// ──────────────────────────────────────────────────
// Problem  : 933. Number of Recent Calls
// Difficulty: Easy
// Tags     : Design, Queue, Data Stream
// Link     : https://leetcode.com/problems/number-of-recent-calls/
// Runtime  : 18 ms (beats 99%)
// Memory   : 53096000 (beats 76%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class RecentCounter {
    private static final int[] records = new int[10000]; //
    private int start;
    private int end;

    public RecentCounter() {        
        start = 0;
        end = 0;
    }
    
    public int ping(int t) {
        while (start < end && (t - records[start] > 3000)) { 
            start++; // if the difference in time is greater than 3000ms, 
            // than increase the value of start unitl it's equal or less than 3000ms.
        }
        records[end++] = t; // Inserting the current time at the end
        return end - start; // Returning the answer including the element added just now.
    }
}