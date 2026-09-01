// ──────────────────────────────────────────────────
// Problem  : 540. Single Element in a Sorted Array
// Difficulty: Medium
// Tags     : Array, Binary Search
// Link     : https://leetcode.com/problems/single-element-in-a-sorted-array/
// Runtime  : 0 ms (beats 100%)
// Memory   : 52800000 (beats 78%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int singleNonDuplicate(int[] nums) {
        int mid,m1,m2;
        int l=0,r=nums.length-1;
        while(l!=r)
        {
            mid=(l+r)/2;
            if(nums[mid]==nums[mid-1])
            {
                m1=(mid-1)-l;
                m2=r-mid;
                if(m1%2!=0)
                {
                    r=mid-2;
                }
                else if(m2%2!=0)
                {
                    l=mid+1;
                }
            }
            else if(nums[mid]==nums[mid+1])
            {
                m1=mid-l;
                m2=r-(mid+1);
                if(m2%2!=0)
                {
                    l=mid+2;
                }
                else if(m1%2!=0)
                {
                    r=mid-1;
                }
            }
            else return nums[mid];
        }
        return nums[l];
    }
}