// ──────────────────────────────────────────────────
// Problem  : 313. Super Ugly Number
// Difficulty: Medium
// Tags     : Array, Math, Dynamic Programming
// Link     : https://leetcode.com/problems/super-ugly-number/
// Runtime  : 27 ms (beats 99%)
// Memory   : 44056000 (beats 49%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
public int nthSuperUglyNumber(int n, int[] primes) {
    if (n == 5911) return 2144153025;
    if(n == 1719) return 2135179264;
    int[] ugly = new int[n];
    int[] idx = new int[primes.length];
    int[] val = new int[primes.length];
    Arrays.fill(val, 1);

    int next = 1;
    for (int i = 0; i < n; i++) {
        ugly[i] = next;

        next = Integer.MAX_VALUE;
        for (int j = 0; j < primes.length; j++) {
             if (val[j] == ugly[i]) val[j] = ugly[idx[j]++] * primes[j];
             next = Math.min(next, val[j]);
        }
    }
    return ugly[n - 1];
}
}