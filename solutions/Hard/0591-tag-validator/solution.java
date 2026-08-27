// ──────────────────────────────────────────────────
// Problem  : 591. Tag Validator
// Difficulty: Hard
// Tags     : String, Stack
// Link     : https://leetcode.com/problems/tag-validator/
// Runtime  : 2 ms (beats 86%)
// Memory   : 43348000 (beats 36%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public boolean isValid(String code) {
        
        Stack<String> stack = new Stack<>(); // TAG_NAME is pushed to stack
        
        for(int i = 0; i < code.length();){
            
            if(i > 0 && stack.isEmpty()) // No TAG_NAME found in the beginning 
                return false;
            
            // <![CDATA[ begin for <![CDATA[CDATA_CONTENT]]>
            if(code.startsWith("<![CDATA[", i)){
                int j = i + 9;
                i = code.indexOf("]]>", j);
                if(i < 0) //if not found "]]>"
                    return false;
                i += 3;
            }
            
            // </TAG_NAME> end
            else if(code.startsWith("</", i)){
                int j = i + 2;
                i = code.indexOf('>', j);
                if(i < 0 || i == j || i - j > 9) 
                    return false;
                for(int k = j; k < i; k++){
                    if(!Character.isUpperCase(code.charAt(k)))  // TAG_NAME only contains upper-case letters
                        return false;
                }
                String s = code.substring(j, i++);
                if(stack.isEmpty() || !stack.pop().equals(s)) // TAG_NAME doesn't match
                    return false;
            }
            
            // <TAG_NAME> begin
            else if(code.startsWith("<", i)){
                int j = i + 1;
                i = code.indexOf('>', j);
                if(i < 0 || i == j || i - j > 9) 
                    return false;
                for(int k = j; k < i; k++){
                    if(!Character.isUpperCase(code.charAt(k))) // TAG_NAME only contains upper-case letters
                        return false;
                }
                String s = code.substring(j, i++);
                stack.push(s);
            }
            
            // All other characters
            else{
                i++;
            }
        }
        return stack.isEmpty();
    }
}