// ──────────────────────────────────────────────────
// Problem  : 166. Fraction to Recurring Decimal
// Difficulty: Medium
// Tags     : Hash Table, Math, String
// Link     : https://leetcode.com/problems/fraction-to-recurring-decimal/
// Runtime  : 2 ms (beats 51%)
// Memory   : 42540000 (beats 80%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.HashMap;
import java.util.Map;

class Solution {
    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();

        // Handle sign
        if ((numerator < 0) ^ (denominator < 0)) {
            result.append("-");
        }

        // Use long to prevent overflow during absolute conversion (e.g., Integer.MIN_VALUE)
        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);

        // Integer part
        result.append(num / den);
        long remainder = num % den;

        if (remainder == 0) {
            return result.toString();
        }

        result.append(".");
        Map<Long, Integer> remainderMap = new HashMap<>();

        while (remainder != 0) {
            if (remainderMap.containsKey(remainder)) {
                result.insert(remainderMap.get(remainder), "(");
                result.append(")");
                break;
            }

            remainderMap.put(remainder, result.length());
            remainder *= 10;
            result.append(remainder / den);
            remainder %= den;
        }

        return result.toString();
    }
}