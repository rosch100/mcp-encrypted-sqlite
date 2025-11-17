package com.example.mcp.sqlite.config;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Encryption class for passphrases using AES-256-GCM.
 * GCM (Galois/Counter Mode) provides authenticated encryption, is secure and fast.
 */
public final class PassphraseEncryption {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 96 bits for GCM
    private static final int GCM_TAG_LENGTH = 16; // 128 bits for authentication
    private static final int KEY_LENGTH = 256; // 256 bits for AES-256
    private static final String ENCRYPTED_PREFIX = "encrypted:";
    
    private final SecretKey secretKey;
    
    /**
     * Creates a PassphraseEncryption instance with a key.
     * Tries macOS Keychain first, then environment variable.
     * 
     * @return PassphraseEncryption instance
     * @throws IllegalStateException if no key is found
     */
    public static PassphraseEncryption fromEnvironment() {
        String keyBase64 = null;
        String keySource = null;
        
        // Try macOS Keychain first (if available)
        if (KeychainKeyStore.isAvailable()) {
            try {
                keyBase64 = KeychainKeyStore.loadKey();
                if (keyBase64 != null && !keyBase64.isEmpty()) {
                    keySource = "Keychain";
                }
            } catch (Exception e) {
                // Ignore errors and try environment variable
                System.err.println("Warning: Error loading from Keychain: " + e.getMessage());
            }
        }
        
        // Fallback to environment variable
        if (keyBase64 == null || keyBase64.isEmpty()) {
            keyBase64 = System.getenv("MCP_SQLITE_ENCRYPTION_KEY");
            if (keyBase64 != null && !keyBase64.isEmpty()) {
                keySource = "Environment variable";
            }
        }
        
        if (keyBase64 == null || keyBase64.isEmpty()) {
            StringBuilder message = new StringBuilder(
                "Encryption key not found. "
            );
            
            if (KeychainKeyStore.isAvailable()) {
                message.append(
                    "Please store a key in macOS Keychain:\n" +
                    "  java -cp <jar> com.example.mcp.sqlite.util.StoreKeyInKeychain <key>\n" +
                    "Or set the environment variable:\n" +
                    "  export MCP_SQLITE_ENCRYPTION_KEY=\"<key>\""
                );
            } else {
                message.append(
                    "Please set the environment variable:\n" +
                    "  export MCP_SQLITE_ENCRYPTION_KEY=\"$(java -cp <jar> com.example.mcp.sqlite.util.GenerateKey)\""
                );
            }
            
            throw new IllegalStateException(message.toString());
        }
        
        // Debug output (only if System Property is set)
        if (System.getProperty("mcp.sqlite.debug") != null) {
            System.err.println("Debug: Encryption key loaded from: " + keySource);
        }
        
        return fromBase64Key(keyBase64);
    }
    
    /**
     * Creates a PassphraseEncryption instance with a key from an environment variable.
     * If the environment variable is not set, a warning is issued and a
     * deterministic key is used (ONLY FOR DEVELOPMENT - NOT FOR PRODUCTION!).
     * 
     * @param allowFallback true to allow a fallback key (development only)
     * @return PassphraseEncryption instance
     * @deprecated Use fromEnvironment() without fallback for production
     */
    @Deprecated
    public static PassphraseEncryption fromEnvironment(boolean allowFallback) {
        String keyBase64 = System.getenv("MCP_SQLITE_ENCRYPTION_KEY");
        if (keyBase64 != null && !keyBase64.isEmpty()) {
            return fromBase64Key(keyBase64);
        }
        if (!allowFallback) {
            throw new IllegalStateException(
                "MCP_SQLITE_ENCRYPTION_KEY environment variable is not set. " +
                "Please set a secure encryption key."
            );
        }
        // Issue warning
        System.err.println("WARNING: MCP_SQLITE_ENCRYPTION_KEY is not set!");
        System.err.println("WARNING: A deterministic fallback key will be used.");
        System.err.println("WARNING: This is INSECURE and should only be used for development!");
        System.err.println("WARNING: For production, please set: export MCP_SQLITE_ENCRYPTION_KEY=\"<key>\"");
        return fromBase64Key(generateDeterministicKey());
    }
    
    /**
     * Creates a PassphraseEncryption instance from a Base64-encoded key.
     * 
     * @param keyBase64 Base64-encoded 256-bit key
     * @return PassphraseEncryption instance
     * @throws IllegalArgumentException if the key is invalid
     */
    public static PassphraseEncryption fromBase64Key(String keyBase64) {
        if (keyBase64 == null || keyBase64.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
        
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(keyBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 format for key", e);
        }
        
        if (keyBytes.length != KEY_LENGTH / 8) {
            throw new IllegalArgumentException(
                String.format("Key must be exactly %d bytes (256 bits) long, but was %d bytes", 
                    KEY_LENGTH / 8, keyBytes.length)
            );
        }
        
        // Check for weak keys (e.g., all zeros or insufficient entropy)
        if (isWeakKey(keyBytes)) {
            throw new IllegalArgumentException(
                "The key is too weak. Please use a randomly generated key."
            );
        }
        
        SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
        return new PassphraseEncryption(key);
    }
    
    /**
     * Checks if a key is too weak (e.g., all zeros or insufficient entropy).
     * 
     * @param keyBytes The key bytes
     * @return true if the key is weak
     */
    private static boolean isWeakKey(byte[] keyBytes) {
        // Check for all zeros
        boolean allZeros = true;
        for (byte b : keyBytes) {
            if (b != 0) {
                allZeros = false;
                break;
            }
        }
        if (allZeros) {
            return true;
        }
        
        // Check for insufficient entropy (e.g., repeating patterns)
        // Simple heuristic: If more than 75% of bytes are equal, the key is weak
        Map<Byte, Integer> byteCounts = new HashMap<>();
        for (byte b : keyBytes) {
            byteCounts.put(b, byteCounts.getOrDefault(b, 0) + 1);
        }
        int maxCount = Collections.max(byteCounts.values());
        if (maxCount > keyBytes.length * 0.75) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Generates a new random encryption key.
     * 
     * @return Base64-encoded key
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_LENGTH);
            SecretKey key = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error generating key", e);
        }
    }
    
    /**
     * Generates a deterministic key for development (testing only).
     * 
     * @return Base64-encoded key
     */
    private static String generateDeterministicKey() {
        // Simple deterministic key for development
        // In production, always use a random key
        String seed = "mcp-sqlite-default-key-development-only";
        byte[] keyBytes = new byte[32];
        byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < 32; i++) {
            keyBytes[i] = seedBytes[i % seedBytes.length];
        }
        return Base64.getEncoder().encodeToString(keyBytes);
    }
    
    private PassphraseEncryption(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
    
    /**
     * Encrypts a passphrase.
     * 
     * @param passphrase The passphrase to encrypt
     * @return Encrypted passphrase with prefix "encrypted:" and Base64 encoding
     */
    public String encrypt(String passphrase) {
        if (passphrase == null || passphrase.isEmpty()) {
            throw new IllegalArgumentException("Passphrase must not be empty");
        }
        
        try {
            // Generate random IV for each encryption
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            byte[] plaintext = passphrase.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(plaintext);
            
            // Combine IV and ciphertext
            byte[] encrypted = new byte[GCM_IV_LENGTH + ciphertext.length];
            System.arraycopy(iv, 0, encrypted, 0, GCM_IV_LENGTH);
            System.arraycopy(ciphertext, 0, encrypted, GCM_IV_LENGTH, ciphertext.length);
            
            return ENCRYPTED_PREFIX + Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting passphrase", e);
        }
    }
    
    /**
     * Decrypts an encrypted passphrase.
     * 
     * @param encryptedPassphrase The encrypted passphrase (with or without prefix)
     * @return The decrypted passphrase
     */
    public String decrypt(String encryptedPassphrase) {
        if (encryptedPassphrase == null || encryptedPassphrase.isEmpty()) {
            throw new IllegalArgumentException("Encrypted passphrase must not be empty");
        }
        
        // Remove prefix if present
        String toDecrypt = encryptedPassphrase.startsWith(ENCRYPTED_PREFIX)
                ? encryptedPassphrase.substring(ENCRYPTED_PREFIX.length())
                : encryptedPassphrase;
        
        try {
            byte[] encrypted = Base64.getDecoder().decode(toDecrypt);
            
            if (encrypted.length < GCM_IV_LENGTH) {
                throw new IllegalArgumentException("Invalid encrypted format");
            }
            
            // Extract IV and ciphertext
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encrypted, 0, iv, 0, GCM_IV_LENGTH);
            
            byte[] ciphertext = new byte[encrypted.length - GCM_IV_LENGTH];
            System.arraycopy(encrypted, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting passphrase", e);
        }
    }
    
    /**
     * Checks if a passphrase is encrypted.
     * 
     * @param passphrase The passphrase to check
     * @return true if encrypted, false otherwise
     */
    public static boolean isEncrypted(String passphrase) {
        return passphrase != null && passphrase.startsWith(ENCRYPTED_PREFIX);
    }
    
    /**
     * Removes the encryption prefix from a passphrase.
     * 
     * @param passphrase The passphrase
     * @return Passphrase without prefix
     */
    public static String removePrefix(String passphrase) {
        if (passphrase != null && passphrase.startsWith(ENCRYPTED_PREFIX)) {
            return passphrase.substring(ENCRYPTED_PREFIX.length());
        }
        return passphrase;
    }
}
