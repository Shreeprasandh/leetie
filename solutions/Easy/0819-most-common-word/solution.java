// ──────────────────────────────────────────────────
// Problem  : 819. Most Common Word
// Difficulty: Easy
// Tags     : Array, Hash Table, String, Counting
// Link     : https://leetcode.com/problems/most-common-word/
// Runtime  : 15 ms (beats 61%)
// Memory   : 44048000 (beats 88%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String mostCommonWord(String paragraph, String[] banned) {
        // 1. Convert entire paragraph to lowercase for case-insensitive comparison
        //    🔡 Normalize all text to same case!
        String low = paragraph.toLowerCase();
        
        // 2. Split into words using regex that matches non-letter characters
        //    ✂️ Split by anything that's NOT a letter!
        String[] words = low.split("[^a-z]+");
        
        // 3. Initialize result variable
        //    🎯 This will hold our final answer!
        String res = "";

        // 4. Create HashMap to store word frequencies
        //    📊 Track how many times each word appears!
        Map<String, Integer> map = new HashMap<>();
        
        // 5. Process each word from the split array
        //    🔄 Iterate through all extracted words!
        for (String word : words) {
            // 6. Skip empty strings that might result from splitting
            //    🚫 Filter out empty results!
            if (word.isEmpty()) continue; 
            
            // 7. Check if current word is in banned list
            //    ⚠️ Verify word isn't prohibited!
            boolean isBanned = false;
            for (String bannedWord : banned) {
                if (word.equals(bannedWord)) {
                    isBanned = true;
                    break;
                }
            }
            
            // 8. If word is not banned, update its frequency count
            //    ✅ Count only allowed words!
            if (!isBanned) {
                map.put(word, map.getOrDefault(word, 0) + 1);
            }
        }
        
        // 9. Find the word with maximum frequency
        //    🏆 Determine the most common word!
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                res = entry.getKey();
            }
        }

        // 10. Return the most frequent non-banned word
        //     🎉 Mission accomplished!
        return res;
    }
}