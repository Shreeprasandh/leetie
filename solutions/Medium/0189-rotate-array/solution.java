// ──────────────────────────────────────────────────
// Problem  : 189. Rotate Array
// Difficulty: Medium
// Tags     : Array, Math, Two Pointers
// Link     : https://leetcode.com/problems/rotate-array/
// Runtime  : 4 ms (beats 4%)
// Memory   : 268812000 (beats 8%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

import java.util.*;
class Solution{
public void rotate(int[]nums,int k){
int n=nums.length;
k%=n;
reverse(nums,0,n-1);
reverse(nums,0,k-1);
reverse(nums,k,n-1);
}
private void reverse(int[]nums,int start,int end){
while(start<end){
int temp=nums[start];
nums[start]=nums[end];
nums[end]=temp;
start++;
end--;
}
}
}