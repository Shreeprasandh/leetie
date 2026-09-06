// ──────────────────────────────────────────────────
// Problem  : 748. Shortest Completing Word
// Difficulty: Easy
// Tags     : Array, Hash Table, String
// Link     : https://leetcode.com/problems/shortest-completing-word/
// Runtime  : 7 ms (beats 29%)
// Memory   : 46828000 (beats 19%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public String shortestCompletingWord(String licensePlate, String[] words) {

/*...This is my brute-force approach!...

        StringBuilder sb=new StringBuilder();
        for(char x:licensePlate.toCharArray()) {
     if(Character.isLetter(x)){
            sb.append(x);
     }
    }
    char []c1=sb.toString().toCharArray();
    Arrays.sort(c1);
String plate=new String(c1);
    // System.out.print(sb.toString());
    System.out.print(new String(c1));
    String res=words[0];
    int minLen=Integer.MAX_VALUE;
    for(String x:words){
        char []temp=x.toCharArray();
        Arrays.sort(temp);
    System.out.println(new String(temp));
String check=new String(temp);
int len=check.length();
if(check.contains(plate)&&len<minLen){
    res=x;
    minLen=len;
}
else if(check.contains(plate)&&len==minLen){
    continue;
}
    }

    */




//here's the optimal version

    licensePlate=licensePlate.toLowerCase();
HashMap<Character,Integer>map=new HashMap<>();
for(char c:licensePlate.toCharArray()){
    if(Character.isLetter(c)){
    map.put(c,map.getOrDefault(c,0)+1);
}
}
String res="";
for(String x:words){
    x=x.toLowerCase();
    boolean b=true;
    for(char ch:map.keySet()){
        int count=0;
        for(char c:x.toCharArray()){
            if(c==ch)count++;
        }
        if(count<map.get(ch)){
            b=false;
            break;
        }
    }
    if(b&&(res.equals("")||x.length()<res.length())){
        res=x;
    }

}
    return res;
    }
}