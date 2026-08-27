// ──────────────────────────────────────────────────
// Problem  : 535. Encode and Decode TinyURL
// Difficulty: Medium
// Tags     : Hash Table, String, Design, Hash Function
// Link     : https://leetcode.com/problems/encode-and-decode-tinyurl/
// Runtime  : 3 ms (beats 71%)
// Memory   : 44432000 (beats 35%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

public class Codec {
    Map<String, String> codeDB = new HashMap<>(), urlDB = new HashMap<>();
    static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private String getCode() {
        char[] code = new char[6];
        for (int i = 0; i < 6; i++) 
            code[i] = chars.charAt((int)(Math.random() * 62));
        return "http://tinyurl.com/" + String.valueOf(code);
    }
    
    public String encode(String longUrl) {
        if (urlDB.containsKey(longUrl)) return urlDB.get(longUrl);
        String code = getCode();
        while (codeDB.containsKey(code)) code = getCode();
        codeDB.put(code, longUrl);
        urlDB.put(longUrl, code);
        return code;
    }

    public String decode(String shortUrl) {
        return codeDB.get(shortUrl);
    }
}