package com.example.mcp.sqlite.config;

import java.nio.file.Path;
import java.util.Objects;

public record DatabaseConfig(Path databasePath, String passphrase, CipherProfile cipherProfile, boolean immutable) {

    public DatabaseConfig {
        Objects.requireNonNull(databasePath, "databasePath");
        Objects.requireNonNull(passphrase, "passphrase");
        Objects.requireNonNull(cipherProfile, "cipherProfile");
    }

    /**
     * Creates a DatabaseConfig with automatic decryption of the passphrase if encrypted.
     *
     * @param databasePath Path to the database
     * @param passphrase Passphrase (can be encrypted with prefix "encrypted:")
     * @param cipherProfile Cipher profile
     * @return DatabaseConfig with decrypted passphrase (immutable=false)
     */
    public static DatabaseConfig withDecryptedPassphrase(Path databasePath, String passphrase, CipherProfile cipherProfile) {
        return withDecryptedPassphrase(databasePath, passphrase, cipherProfile, false);
    }

    /**
     * Creates a DatabaseConfig with automatic decryption of the passphrase if encrypted.
     *
     * @param databasePath Path to the database
     * @param passphrase Passphrase (can be encrypted with prefix "encrypted:")
     * @param cipherProfile Cipher profile
     * @param immutable When true, open via SQLite URI {@code immutable=1} (no file locks)
     * @return DatabaseConfig with decrypted passphrase
     */
    public static DatabaseConfig withDecryptedPassphrase(
            Path databasePath,
            String passphrase,
            CipherProfile cipherProfile,
            boolean immutable) {
        String decryptedPassphrase = decryptPassphraseIfNeeded(passphrase);
        return new DatabaseConfig(databasePath, decryptedPassphrase, cipherProfile, immutable);
    }

    /**
     * Decrypts a passphrase if it is encrypted.
     *
     * @param passphrase The passphrase (can be encrypted)
     * @return The decrypted passphrase or the original passphrase if not encrypted
     * @throws IllegalStateException if encrypted passphrase is used but MCP_SQLITE_ENCRYPTION_KEY is not set
     */
    public static String decryptPassphraseIfNeeded(String passphrase) {
        if (passphrase == null) {
            return null;
        }

        if (PassphraseEncryption.isEncrypted(passphrase)) {
            try {
                PassphraseEncryption encryption = PassphraseEncryption.fromEnvironment();
                String decrypted = encryption.decrypt(passphrase);
                if (decrypted == null || decrypted.isEmpty()) {
                    throw new IllegalStateException("Decrypted passphrase is empty");
                }
                return decrypted;
            } catch (IllegalStateException e) {
                throw new IllegalStateException(
                    "Encrypted passphrase detected, but MCP_SQLITE_ENCRYPTION_KEY is not set. " +
                    "Please set the environment variable with the encryption key.", e
                );
            } catch (Exception e) {
                throw new IllegalStateException(
                    "Error decrypting passphrase: " + e.getMessage(), e
                );
            }
        }

        return passphrase;
    }
}
