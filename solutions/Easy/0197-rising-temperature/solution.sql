-- ──────────────────────────────────────────────────
-- Problem  : 197. Rising Temperature
-- Difficulty: Easy
-- Tags     : Database
-- Link     : https://leetcode.com/problems/rising-temperature/
-- Runtime  : 66 ms (beats 0%)
-- Memory   : 0B (beats 0%)
-- Language : mysql
-- Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
-- Synced by: leetie
-- ──────────────────────────────────────────────────

SELECT w1.id
FROM Weather w1, Weather w2
WHERE DATEDIFF(w1.recordDate, w2.recordDate) = 1 AND w1.temperature > w2.temperature;