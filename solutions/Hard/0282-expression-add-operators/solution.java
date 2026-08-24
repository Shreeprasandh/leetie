// ──────────────────────────────────────────────────
// Problem  : 282. Expression Add Operators
// Difficulty: Hard
// Tags     : Math, String, Backtracking
// Link     : https://leetcode.com/problems/expression-add-operators/
// Runtime  : 77 ms (beats 76%)
// Memory   : 47040000 (beats 84%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<String> addOperators(String num, int target) {
        List<String> result = new ArrayList<>();
        if (num == null || num.length() == 0) return result;
        backtrack(result, new StringBuilder(), num, target, 0, 0, 0);
        return result;
    }

    private void backtrack(List<String> result, StringBuilder path, String num, int target, int index, long eval, long multed) {
        if (index == num.length()) {
            if (eval == target) {
                result.add(path.toString());
            }
            return;
        }

        for (int i = index; i < num.length(); i++) {
            if (i != index && num.charAt(index) == '0') break; // Avoid leading zeros
            
            long curr = Long.parseLong(num.substring(index, i + 1));
            int len = path.length();

            if (index == 0) {
                path.append(curr);
                backtrack(result, path, num, target, i + 1, curr, curr);
                path.setLength(len);
            } else {
                path.append("+").append(curr);
                backtrack(result, path, num, target, i + 1, eval + curr, curr);
                path.setLength(len);

                path.append("-").append(curr);
                backtrack(result, path, num, target, i + 1, eval - curr, -curr);
                path.setLength(len);

                path.append("*").append(curr);
                backtrack(result, path, num, target, i + 1, eval - multed + multed * curr, multed * curr);
                path.setLength(len);
            }
        }
    }
}