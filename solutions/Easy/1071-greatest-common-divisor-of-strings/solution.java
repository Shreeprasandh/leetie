// ──────────────────────────────────────────────────
// Problem  : 1071. Greatest Common Divisor of Strings
// Difficulty: Easy
// Tags     : Math, String, Euclidean Algorithm, Greatest Common Divisor
// Link     : https://leetcode.com/problems/greatest-common-divisor-of-strings/
// Runtime  : 1 ms (beats 95%)
// Memory   : 43404000 (beats 75%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String gcdOfStrings(String str1, String str2) {
        if(!(str1 + str2).equals(str2 + str1)){
            return "";
        }

        int gcdLength = getGCD(str1.length() , str2.length());
        return str1.substring(0,gcdLength);
    }

    private int getGCD(int a , int b){
        while(b!=0){
            int temp = b;
            b=a%b;
            a=temp;
        }
        return a;
    }
}