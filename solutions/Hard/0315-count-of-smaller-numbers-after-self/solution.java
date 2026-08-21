// ──────────────────────────────────────────────────
// Problem  : 315. Count of Smaller Numbers After Self
// Difficulty: Hard
// Tags     : Array, Binary Search, Divide and Conquer, Binary Indexed Tree, Segment Tree, Merge Sort, Ordered Set, Treap
// Link     : https://leetcode.com/problems/count-of-smaller-numbers-after-self/
// Runtime  : 67 ms (beats 58%)
// Memory   : 91008000 (beats 90%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    private int[] count;
    
    public List<Integer> countSmaller(int[] nums) {
        count = new int[nums.length];
        int[] indices = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            indices[i] = i;
        }
        
        mergeSort(nums, indices, 0, nums.length - 1);
        
        List<Integer> result = new ArrayList<>(nums.length);
        for (int c : count) {
            result.add(c);
        }
        return result;
    }
    
    private void mergeSort(int[] nums, int[] indices, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        
        mergeSort(nums, indices, left, mid);
        mergeSort(nums, indices, mid + 1, right);
        merge(nums, indices, left, mid, right);
    }
    
    private void merge(int[] nums, int[] indices, int left, int mid, int right) {
        int[] tempIndices = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        int rightCount = 0;
        
        while (i <= mid && j <= right) {
            if (nums[indices[j]] < nums[indices[i]]) {
                rightCount++;
                tempIndices[k++] = indices[j++];
            } else {
                count[indices[i]] += rightCount;
                tempIndices[k++] = indices[i++];
            }
        }
        
        while (i <= mid) {
            count[indices[i]] += rightCount;
            tempIndices[k++] = indices[i++];
        }
        
        while (j <= right) {
            tempIndices[k++] = indices[j++];
        }
        
        for (int p = 0; p < tempIndices.length; p++) {
            indices[left + p] = tempIndices[p];
        }
    }
}