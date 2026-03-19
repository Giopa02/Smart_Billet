package org.example;

public class GenererHash {
    public static void main(String[] args) {
        String hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                .hashToString(12, "admin123".toCharArray());
        System.out.println("Hash : " + hash);
    }
}