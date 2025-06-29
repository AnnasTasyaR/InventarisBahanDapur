package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for hashing strings using SHA-256.
 */
public class HashUtil {

    /**
     * Hashes the given password using SHA-256 algorithm.
     *
     * @param password The plain text password.
     * @return The hashed password in hexadecimal format.
     */
    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert byte[] to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
