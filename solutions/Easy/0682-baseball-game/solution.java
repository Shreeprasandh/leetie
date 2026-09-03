// ──────────────────────────────────────────────────
// Problem  : 682. Baseball Game
// Difficulty: Easy
// Tags     : Array, Stack, Simulation
// Link     : https://leetcode.com/problems/baseball-game/
// Runtime  : 2 ms (beats 96%)
// Memory   : 43424000 (beats 67%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int calPoints(String[] operations) {
        Deque<Integer> stack = new ArrayDeque<>();

        for(String op : operations){
            if(op.equals("C")) stack.pop();
            else if(op.equals("D")) stack.push(stack.peek()*2);
            else if(op.equals("+")){
                int first = stack.pop();
                int second = stack.peek();

                stack.push(first);
                stack.push(first + second);
            }else{
                stack.push(Integer.parseInt(op));
            }
        }
        int sum = 0;

        while(!stack.isEmpty()){
            sum+=stack.pop();
        }
        return sum;
    }
}