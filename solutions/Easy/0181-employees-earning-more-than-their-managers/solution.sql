-- ──────────────────────────────────────────────────
-- Problem  : 181. Employees Earning More Than Their Managers
-- Difficulty: Easy
-- Tags     : Database
-- Link     : https://leetcode.com/problems/employees-earning-more-than-their-managers/
-- Runtime  : 432 ms (beats 43%)
-- Memory   : 0B (beats 100%)
-- Language : mysql
-- Copyright: (c) 2026 Shreeprasandh. All rights reserved.
-- Synced by: leetie
-- ──────────────────────────────────────────────────

# Write your MySQL query statement below
SELECT a.name AS Employee
FROM Employee AS a
JOIN Employee AS b
  ON a.managerId = b.id
WHERE a.salary > b.salary;