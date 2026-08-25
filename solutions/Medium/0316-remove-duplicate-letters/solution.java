// ──────────────────────────────────────────────────
// Problem  : 316. Remove Duplicate Letters
// Difficulty: Medium
// Tags     : String, Stack, Greedy, Monotonic Stack
// Link     : https://leetcode.com/problems/remove-duplicate-letters/
// Runtime  : 2 ms (beats 82%)
// Memory   : 43412000 (beats 85%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String removeDuplicateLetters(String s) {
        int[] lastIndex = new int[26];
        for (int i = 0; i < s.length(); i++){
            lastIndex[s.charAt(i) - 'a'] = i; // track the lastIndex of character presence
        }
        
        boolean[] seen = new boolean[26]; // keep track seen
        Stack<Integer> st = new Stack();
        
        for (int i = 0; i < s.length(); i++) {
            int curr = s.charAt(i) - 'a';
            if (seen[curr]) continue; // if seen continue as we need to pick one char only
            while (!st.isEmpty() && st.peek() > curr && i < lastIndex[st.peek()]){
                seen[st.pop()] = false; // pop out and mark unseen
            }
            st.push(curr); // add into stack
            seen[curr] = true; // mark seen
        }

        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty())
            sb.append((char) (st.pop() + 'a'));
        return sb.reverse().toString();
    }
}