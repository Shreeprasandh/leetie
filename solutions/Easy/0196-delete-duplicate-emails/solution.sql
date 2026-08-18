-- ──────────────────────────────────────────────────
-- Problem  : 196. Delete Duplicate Emails
-- Difficulty: Easy
-- Tags     : Database
-- Link     : https://leetcode.com/problems/delete-duplicate-emails/
-- Runtime  : 67 ms (beats 0%)
-- Memory   : 0B (beats 0%)
-- Language : mysql
-- Copyright: (c) 2026 Shreeprasandh K. All rights reserved.
-- Synced by: leetie
-- ──────────────────────────────────────────────────

delete p1 from person p1,person p2 
where p1.email=p2.email and p1.id>p2.id;