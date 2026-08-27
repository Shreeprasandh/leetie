// ──────────────────────────────────────────────────
// Problem  : 500. Keyboard Row
// Difficulty: Easy
// Tags     : Array, Hash Table, String
// Link     : https://leetcode.com/problems/keyboard-row/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42768000 (beats 69%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String[] findWords(String[] words) {
       ArrayList<String> ans=new ArrayList<>();
       String first = "qwertyuiop";
       String Secound ="asdfghjkl";
       String Third="zxcvbnm";
       for(String i : words){
        if(isinrow(i,first) || isinrow(i,Secound) || isinrow(i,Third))
         ans.add(i);
       } 
       return ans.toArray(new String[0]);
    }
    private boolean isinrow(String s,String row){
      for(char c:s.toCharArray()){
        if(row.indexOf(Character.toLowerCase(c))==-1){
            return false;
        }
      }
      return true;
    }
}