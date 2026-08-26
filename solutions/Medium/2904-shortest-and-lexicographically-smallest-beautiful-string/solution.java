// ──────────────────────────────────────────────────
// Problem  : 2904. Shortest and Lexicographically Smallest Beautiful String
// Difficulty: Medium
// Tags     : String, Sliding Window
// Link     : https://leetcode.com/problems/shortest-and-lexicographically-smallest-beautiful-string/
// Runtime  : 6 ms (beats 10%)
// Memory   : 45488000 (beats 9%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String shortestBeautifulSubstring(String s, int k) {
        String ans = "";
        int n = s.length();

        for (int i = 0; i < n; i++) {

            int oneCnt = 0;
            StringBuilder cur = new StringBuilder();

            for (int j = i; j < n; j++) {

                cur.append(s.charAt(j));

                if (s.charAt(j) == '1')
                    oneCnt++;

                // More than k ones can never become valid again
                if (oneCnt > k)
                    break;

                if (oneCnt == k) {
                    String curStr = cur.toString();

                    if (ans.isEmpty() ||
                        curStr.length() < ans.length() ||
                        (curStr.length() == ans.length() && curStr.compareTo(ans) < 0)) {

                        ans = curStr;
                    }
                }
            }
        }

        return ans;
    }
}