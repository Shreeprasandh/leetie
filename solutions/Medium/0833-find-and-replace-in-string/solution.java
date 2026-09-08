// ──────────────────────────────────────────────────
// Problem  : 833. Find And Replace in String
// Difficulty: Medium
// Tags     : Array, Hash Table, String, Sorting
// Link     : https://leetcode.com/problems/find-and-replace-in-string/
// Runtime  : 2 ms (beats 77%)
// Memory   : 44048000 (beats 20%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String findReplaceString(String s, int[] indices, String[] sources, String[] targets) {
        Map<Integer,Integer> map=new HashMap<>();
        for (int i=0; i<indices.length; i++){
            if (s.startsWith(sources[i],indices[i])){
                map.put(indices[i],i);
            }
        }  
        StringBuilder sb=new StringBuilder();
        for (int i=0; i<s.length();){
            if (!map.containsKey(i)){
                sb.append(s.charAt(i));
                i++;
            } else { //replace chars
                sb.append(targets[map.get(i)]); 
                i+=sources[map.get(i)].length();
            }
        }
        return sb.toString();
    }
}