// CryptoUtil.java
package util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    // AES
    public static String encryptAES(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptAES(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted);
    }

    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    // Reverse Encryption (Demo)
    public static String reverseEncrypt(String data) {
        return new StringBuilder(data).reverse().toString();
    }

    public static String reverseDecrypt(String data) {
        return new StringBuilder(data).reverse().toString();
    }

    // Hashing
    public static String hashMD5(String data) throws Exception {
        return hash(data, "MD5");
    }

    public static String hashSHA256(String data) throws Exception {
        return hash(data, "SHA-256");
    }

    private static String hash(String data, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hash = digest.digest(data.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    // Hashing with Salt
    public static String hashWithSalt(String data, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashed = md.digest(data.getBytes());
        return Base64.getEncoder().encodeToString(hashed);
    }

    public static byte[] getSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
} 
