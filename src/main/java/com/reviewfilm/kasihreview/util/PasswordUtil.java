package com.reviewfilm.kasihreview.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {
    
    private static final int DEFAULT_ITERATIONS = 100_000;
    private static final int DEFAULT_KEY_LENGTH = 256;
    private static final int DEFAULT_SALT_LENGTH = 16;
    
    /**
     * Generate random salt
     */
    public static byte[] generateSalt() {
        return generateSalt(DEFAULT_SALT_LENGTH);
    }
    
    /**
     * Generate random salt with custom length
     */
    public static byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
    
    /**
     * Hash password using PBKDF2
     */
    public static String hashPassword(String password, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return hashPassword(password, salt, DEFAULT_ITERATIONS, DEFAULT_KEY_LENGTH);
    }
    
    /**
     * Hash password using PBKDF2 with custom parameters
     */
    public static String hashPassword(String password, byte[] salt, int iterations, int keyLength) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
    
    /**
     * Verify password against stored hash
     * @param password Password to verify
     * @param storedHash Stored hash from database
     * @param salt Salt used for hashing
     * @return true if password matches
     */
    public static boolean verifyPassword(String password, String storedHash, byte[] salt) {
        try {
            String hashToCheck = hashPassword(password, salt);
            return hashToCheck.equals(storedHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Log the exception in production for debugging
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Create a combined string of salt and hash for storage
     * Format: base64(salt):base64(hash)
     */
    public static String createPasswordEntry(String password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt();
        String hash = hashPassword(password, salt);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        return saltBase64 + ":" + hash;
    }
    
    /**
     * Verify password against combined storage format
     * @param password Password to verify
     * @param storedEntry Combined salt:hash from database
     * @return true if password matches
     */
    public static boolean verifyPasswordFromEntry(String password, String storedEntry) {
        try {
            String[] parts = storedEntry.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];
            
            return verifyPassword(password, storedHash, salt);
        } catch (IllegalArgumentException e) {
            // Base64 decoding failed or invalid format
            System.err.println("Error decoding stored entry: " + e.getMessage());
            return false;
        }
    }
}