// ──────────────────────────────────────────────────
// Problem  : 467. Unique Substrings in Wraparound String
// Difficulty: Medium
// Tags     : String, Dynamic Programming
// Link     : https://leetcode.com/problems/unique-substrings-in-wraparound-string/
// Runtime  : 9 ms (beats 88%)
// Memory   : 44476000 (beats 96%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

 
 class Solution {
    public int findSubstringInWraproundString(String p) {
        int[] dp= new int[26];
        int continious=0;
        for(int i=0;i<p.length();i++){
            if(i>0 && ( p.charAt(i)-p.charAt(i-1)==1 || p.charAt(i-1)-p.charAt(i)==25)){
                continious++;
            }
            else{
                continious=1;
            }
            int index=p.charAt(i)-'a';
            dp[index]=Math.max(dp[index],continious);
        }
        
        int ans=0;
        for(int i: dp){
            ans+=i;
        }
        
        return ans;
    }
}