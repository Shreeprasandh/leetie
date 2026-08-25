// ──────────────────────────────────────────────────
// Problem  : 345. Reverse Vowels of a String
// Difficulty: Easy
// Tags     : Two Pointers, String
// Link     : https://leetcode.com/problems/reverse-vowels-of-a-string/
// Runtime  : 2 ms (beats 99%)
// Memory   : 46692000 (beats 49%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String reverseVowels(String s) {

        char[] chars = s.toCharArray();

        int start = 0 ;

        int end = s.length()-1;

        while (start<end){
            while (start < end && !isVowel(chars[start])) {
                start++;
            }

            while (start < end && !isVowel(chars[end])) {
                end--;
            }

            if (start < end) {
                swap(chars, start, end);
                start++;
                end--;
            }

        }

        return new String(chars);
    }

    private void swap(char[] word, int start, int end){
        char temp = word[start];
        word[start] = word[end];
        word[end] = temp;
    }

    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
                || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }
}