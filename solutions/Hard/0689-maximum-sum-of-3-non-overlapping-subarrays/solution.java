// ──────────────────────────────────────────────────
// Problem  : 689. Maximum Sum of 3 Non-Overlapping Subarrays
// Difficulty: Hard
// Tags     : Array, Dynamic Programming, Sliding Window, Prefix Sum
// Link     : https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
// Runtime  : 19 ms (beats 11%)
// Memory   : 53896000 (beats 6%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {

        int n = nums.length;
        int[] prefix = new int[n+1];

        for(int i=0; i<n; i++)
            prefix[i+1] = prefix[i] + nums[i];

        int[][][] dp = new int[n+1][4][3];

        int result = Integer.MIN_VALUE;
        int idx = -1;

        for(int i=n-1; i>=0; i--){
            for(int j=0; j<3; j++){
                if((3 - j) * k <= n - i){
                    int sum = prefix[i+k]-prefix[i] + dp[i+k][j+1][0];
                    if(sum >= dp[i+1][j][0]){
                        dp[i][j][0] = sum;
                        dp[i][j][1] = i;
                        dp[i][j][2] = j==2 ? -1 : dp[i+k][j+1][1];
                    }else{
                        dp[i][j] = dp[i+1][j];
                    }

                    if(j == 0 && result <= dp[i][j][0]){
                        result = dp[i][j][0];
                        idx = dp[i][j][1];
                    }
                }
            }
        }

        int[] res = new int[3];
        for(int i=0; i<3; i++){
            res[i] = idx;
            idx = dp[idx][i][2];
        }

        return res;
    }
}