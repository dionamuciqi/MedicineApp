package com.example.medicineapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    private static final String SALT = "SUDO";

    public static String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String inputWithSalt = input + SALT;
            byte[] hashBytes = md.digest(inputWithSalt.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
