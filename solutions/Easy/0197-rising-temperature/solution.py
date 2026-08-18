# ──────────────────────────────────────────────────
# Problem  : 197. Rising Temperature
# Difficulty: Easy
# Tags     : Database
# Link     : https://leetcode.com/problems/rising-temperature/
# Runtime  : N/A (beats 0%)
# Memory   : N/A (beats 0%)
# Language : python3
# Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
# Synced by: leetie
# ──────────────────────────────────────────────────

SELECT w1.id
FROM Weather w1, Weather w2
WHERE DATEDIFF(w1.recordDate, w2.recordDate) = 1 AND w1.temperature > w2.temperature;