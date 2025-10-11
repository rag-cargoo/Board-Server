package com.example.boardserver.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SHA256Util {

    public static final String ENCRYPTION_KEY = "SHA-256";

    public static String encryptionSHA256(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPTION_KEY);
            byte[] digest = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to apply SHA-256 encryption", e);
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
