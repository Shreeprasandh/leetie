// ──────────────────────────────────────────────────
// Problem  : 227. Basic Calculator II
// Difficulty: Medium
// Tags     : Math, String, Stack
// Link     : https://leetcode.com/problems/basic-calculator-ii/
// Runtime  : 14 ms (beats 89%)
// Memory   : 46268000 (beats 96%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int calculate(String s) {
        if (s == null || s.isEmpty()) return 0;
        
        int length = s.length();
        int currentNumber = 0;
        int lastNumber = 0;
        int result = 0;
        char operator = '+';
        
        for (int i = 0; i < length; i++) {
            char currentChar = s.charAt(i);
            
            if (Character.isDigit(currentChar)) {
                currentNumber = (currentNumber * 10) + (currentChar - '0');
            }
            
            if (!Character.isDigit(currentChar) && !Character.isWhitespace(currentChar) || i == length - 1) {
                if (operator == '+') {
                    result += lastNumber;
                    lastNumber = currentNumber;
                } else if (operator == '-') {
                    result += lastNumber;
                    lastNumber = -currentNumber;
                } else if (operator == '*') {
                    lastNumber = lastNumber * currentNumber;
                } else if (operator == '/') {
                    lastNumber = lastNumber / currentNumber;
                }
                
                operator = currentChar;
                currentNumber = 0;
            }
        }
        
        result += lastNumber;
        return result;
    }
}