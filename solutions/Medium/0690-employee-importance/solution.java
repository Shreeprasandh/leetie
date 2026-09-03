// ──────────────────────────────────────────────────
// Problem  : 690. Employee Importance
// Difficulty: Medium
// Tags     : Array, Hash Table, Tree, Depth-First Search, Breadth-First Search
// Link     : https://leetcode.com/problems/employee-importance/
// Runtime  : 2 ms (beats 99%)
// Memory   : 47848000 (beats 78%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {
    public int getImportance(List<Employee> employees, int id) {
        Map<Integer, Employee> inputMap = new HashMap<>();
		// Construct HashMap as getting the employee from id is difficult in a list
		for(Employee e : employees) {
			inputMap.put(e.id, e);
		}
		return helper(inputMap, id);
	}

	private static int helper(Map<Integer, Employee> inputMap, int id) {
		//Get the importance of the employee
		int imp = inputMap.get(id).importance;
		
		//Add importance of subordinates to employee importance
		for(int subId : inputMap.get(id).subordinates) {
			imp += helper(inputMap, subId);
		}
		
		return imp;
	}
}