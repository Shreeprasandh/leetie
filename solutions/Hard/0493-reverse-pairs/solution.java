// ──────────────────────────────────────────────────
// Problem  : 493. Reverse Pairs
// Difficulty: Hard
// Tags     : Array, Binary Search, Divide and Conquer, Binary Indexed Tree, Segment Tree, Merge Sort, Ordered Set, Treap
// Link     : https://leetcode.com/problems/reverse-pairs/
// Runtime  : 44 ms (beats 55%)
// Memory   : 60788000 (beats 67%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int reversePairs(int[] nums) {
        return mergeSort(nums,0,nums.length-1);
    }

    public static int mergeSort(int[] arr, int l, int r) {
        if(l < r) {
            int m = l + (r - l) / 2;
            int count = mergeSort(arr,l,m) + mergeSort(arr,m+1,r);
            int j = m + 1;
            for(int i=l;i<=m;i++) {
                while(j <= r && (long)arr[i] >2L*arr[j]) {
                    j++;
                }
                count += j - (m + 1);
            }
            merge(arr,l,m,r);
            return count;
        }
        else {
            return 0;
        }
    }

    public static void merge(int[] arr, int l, int m, int r) {
        int n1 = (m - l + 1);
        int n2 = (r - m);
        int left[] = new int[n1];
        int right[] = new int[n2];

        for(int i=0;i<n1;i++) {
            left[i] = arr[l+i];
        }
        for(int j=0;j<n2;j++) {
            right[j] = arr[m+1+j];
        }

        int i=0,j=0,k=l;
        while(i < n1 && j < n2) {
            if(left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
            }
            else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }

        while(i < n1) {
            arr[k] = left[i];
            i++;
            k++;
        }
        while(j < n2) {
            arr[k] = right[j];
            j++;
            k++;
        }
    }
}