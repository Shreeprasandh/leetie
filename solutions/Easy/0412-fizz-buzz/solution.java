// ──────────────────────────────────────────────────
// Problem  : 412. Fizz Buzz
// Difficulty: Easy
// Tags     : Math, String, Simulation
// Link     : https://leetcode.com/problems/fizz-buzz/
// Runtime  : 1 ms (beats 100%)
// Memory   : 46760000 (beats 66%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public List<String> fizzBuzz(int n) {
        List<String> ans = new ArrayList<>();

        for(int i=1; i<=n; i++) {
            if(i%3 ==0 && i%5==0) {
                ans.add("FizzBuzz");
            }
            else if(i%3==0) {
                ans.add("Fizz");
            }
            else if(i%5==0) {
                ans.add("Buzz");
            }
            else {
                ans.add(Integer.toString(i));
            }
        }
        return ans;
    }
}