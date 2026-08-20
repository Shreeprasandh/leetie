// ──────────────────────────────────────────────────
// Problem  : 204. Count Primes
// Difficulty: Medium
// Tags     : Array, Math, Enumeration, Number Theory, Primality Test, Sieve Theory, Prime Number Sieve
// Link     : https://leetcode.com/problems/count-primes/
// Runtime  : 677 ms (beats 5%)
// Memory   : 79340000 (beats 7%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int countPrimes(int n) {
        boolean[] seen = new boolean[n];
        int ans = 0;
        for (int num = 2; num < n; num++) {
            if (seen[num]) continue;
            ans += 1;
            for (long mult = (long)num * num; mult < n; mult += num)
                seen[(int)mult] = true;
        }
        return ans;
    }
}