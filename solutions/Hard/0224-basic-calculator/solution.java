// ──────────────────────────────────────────────────
// Problem  : 224. Basic Calculator
// Difficulty: Hard
// Tags     : Math, String, Stack, Recursion
// Link     : https://leetcode.com/problems/basic-calculator/
// Runtime  : 11 ms (beats 60%)
// Memory   : 46136000 (beats 93%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int calculate(String s) {
        java.util.Stack<Integer> stack = new java.util.Stack<>();
        int currentNumber = 0;
        int result = 0;
        int sign = 1; // 1 means positive, -1 means negative

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isDigit(c)) {
                currentNumber = currentNumber * 10 + (c - '0');
            } else if (c == '+') {
                result += sign * currentNumber;
                currentNumber = 0;
                sign = 1;
            } else if (c == '-') {
                result += sign * currentNumber;
                currentNumber = 0;
                sign = -1;
            } else if (c == '(') {
                // Push the current result and sign onto the stack
                stack.push(result);
                stack.push(sign);
                // Reset for the inner expression
                result = 0;
                sign = 1;
            } else if (c == ')') {
                result += sign * currentNumber;
                currentNumber = 0;
                // Multiply by the sign before the parenthesis
                result *= stack.pop();
                // Add the result computed before the parenthesis
                result += stack.pop();
            }
        }

        result += sign * currentNumber;
        return result;
    }
}