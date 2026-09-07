// ──────────────────────────────────────────────────
// Problem  : 813. Largest Sum of Averages
// Difficulty: Medium
// Tags     : Array, Dynamic Programming, Prefix Sum
// Link     : https://leetcode.com/problems/largest-sum-of-averages/
// Runtime  : 3 ms (beats 84%)
// Memory   : 45604000 (beats 86%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public double largestSumOfAverages(int[] nums, int k) {
        int n = nums.length;
        double dp[][]=new double[n][k+1];
        for(int i=0;i<n;i++)
        {
            Arrays.fill(dp[i],-1);
        }
        return recur(nums,0,k,dp);
    }
    static double recur(int nums[],int idx,int k,double dp[][])
    {
        int n = nums.length;
        if(idx==n)return 0;
        if(dp[idx][k]!=-1)return dp[idx][k];
        if(k==1)
        {
            double sum=0;
            for(int i=idx;i<n;i++)
            {
                sum+=nums[i];
            }
            return dp[idx][k]=sum/(n-idx);
        }
        double ans = 0.0;
        double sum = 0.0;
            for(int j=idx;j<=n-k;j++)
            {
                sum+=nums[j];
                double avg = sum/(j-idx+1);
                ans = Math.max(ans,avg+recur(nums,j+1,k-1,dp));
            }
        return dp[idx][k]=ans;
    }
}