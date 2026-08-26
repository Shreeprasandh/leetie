// ──────────────────────────────────────────────────
// Problem  : 468. Validate IP Address
// Difficulty: Medium
// Tags     : String
// Link     : https://leetcode.com/problems/validate-ip-address/
// Runtime  : 3 ms (beats 21%)
// Memory   : 43096000 (beats 38%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {

    static class State {
        private final StringBuilder buffer = new StringBuilder(4);

        static boolean in(int that, int l, int h) {
            return l <= that && that <= h;
        }

        static boolean isDec(int that) {
            return in(that, '0', '9');
        }

        static boolean isHex(int that) {
            return isDec(that) ||
                    in(that, 'A', 'F') ||
                    in(that, 'a', 'f');
        }

        boolean putAnyNoLengthCheck(char ch) {
            if (!isHex(ch)) {
                return false;
            }
            buffer.append(ch);
            return true;
        }

        boolean putV4or6(char ch) {
            return buffer.length() < 4 && putAnyNoLengthCheck(ch);
        }

        boolean putV4(char ch) {
            boolean doPut = buffer.length() < 3 && isDec(ch);
            if (doPut) {
                buffer.append(ch);
            }
            return doPut;
        }

        boolean hasNonDecChars() {
            return !buffer.chars().allMatch(State::isDec);
        }

        private void clear() {
            buffer.setLength(0);
        }

        boolean isValidV4Part() {
            if (buffer.isEmpty()) {
                return false;
            }
            if (buffer.length() > 1 && buffer.charAt(0) == '0') {
                return false;
            }
            int parsed = Integer.parseUnsignedInt(buffer, 0, buffer.length(), 10);
            clear();
            return parsed <= 255;
        }

        boolean isValidV6Part() {
            if (buffer.isEmpty()) {
                return false;
            }
            int parsed = Integer.parseUnsignedInt(buffer, 0, buffer.length(), 16);
            clear();
            return parsed <= 0xFFFF;
        }

    }

    public String validIPAddress(String text) {
        if (text.length() < 7) {
            return "Neither";
        }

        var state = new State();

        // lookup first for the first delimiter
        if (!state.putAnyNoLengthCheck(text.charAt(0))) {
            return "Neither";
        }

        boolean isV4 = false;
        int pos = 1;
        while (pos < text.length()) {
            char letter = text.charAt(pos++);
            if (letter == '.') {
                isV4 = true;
                break;
            }

            if (letter == ':') {
                break;
            }

            if (!state.putV4or6(letter)) {
                return "Neither";
            }
        }

        if (isV4) {
            if (state.hasNonDecChars() || !state.isValidV4Part()) {
                return "Neither";
            }

            int partsCount = 1;

            while (pos < text.length()) {
                char letter = text.charAt(pos++);
                if (letter == '.') {
                    if (++partsCount == 4 || !state.isValidV4Part()) {
                        return "Neither";
                    }
                } else {
                    if (!state.putV4(letter)) {
                        return "Neither";
                    }
                }
            }

            if (++partsCount == 4 && state.isValidV4Part()) {
                return "IPv4";
            } else {
                return "Neither";
            }
        } else {
            if (!state.isValidV6Part()) {
                return "Neither";
            }

            int partsCount = 1;

            while (pos < text.length()) {
                char letter = text.charAt(pos++);
                if (letter == ':') {
                    if (++partsCount == 8 || !state.isValidV6Part()) {
                        return "Neither";
                    }
                } else {
                    if (!state.putV4or6(letter)) {
                        return "Neither";
                    }
                }
            }

            if (++partsCount == 8 && state.isValidV6Part()) {
                return "IPv6";
            } else {
                return "Neither";
            }
        }
    }
}