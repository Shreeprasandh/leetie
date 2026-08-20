// ──────────────────────────────────────────────────
// Problem  : 3069. Distribute Elements Into Two Arrays I
// Difficulty: Easy
// Tags     : Array, Simulation
// Link     : https://leetcode.com/problems/distribute-elements-into-two-arrays-i/
// Runtime  : 1 ms (beats 98%)
// Memory   : 46424000 (beats 91%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] resultArray(int[] nums) {
        int n = nums.length;

        int[] res = new int[n];

        int i = 0;
        int j = n - 1;

        res[i++] = nums[0];

        if(n == 1){
            return res;
        }

        res[j--] = nums[1];

        for(int k = 2; k < n; k++){
            if(res[i - 1] > res[j + 1]){
                res[i++] = nums[k];
            } else {
                res[j--] = nums[k];
            }
        }

        // Reverse the second array
        int left = j + 1;
        int right = n - 1;

        while(left < right){
            int temp = res[left];
            res[left] = res[right];
            res[right] = temp;

            left++;
            right--;
        }

        return res;
    }
}