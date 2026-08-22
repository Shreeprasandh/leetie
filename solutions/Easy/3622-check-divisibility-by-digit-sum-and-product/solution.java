// ──────────────────────────────────────────────────
// Problem  : 3622. Check Divisibility by Digit Sum and Product
// Difficulty: Easy
// Tags     : Math
// Link     : https://leetcode.com/problems/check-divisibility-by-digit-sum-and-product/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42324000 (beats 62%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean checkDivisibility(int n) {
        int original = n;
        int digitSum = 0;
        int digitProduct = 1;

        while (n > 0) {
            int digit = n % 10;
            digitSum += digit;
            digitProduct *= digit;
            n /= 10;
        }

        int divisor = digitSum + digitProduct;
        return original % divisor == 0;
    }
}