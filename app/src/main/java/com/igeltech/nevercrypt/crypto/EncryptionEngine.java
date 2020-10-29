package com.igeltech.nevercrypt.crypto;

public interface EncryptionEngine
{
    /**
     * Initializes engine
     *
     * @throws EncryptionEngineException on init error
     */
    void init() throws EncryptionEngineException;

    /**
     * Decrypts buffer
     *
     * @param data data bytes array
     * @throws EncryptionEngineException on init error
     */
    void decrypt(byte[] data, int offset, int len) throws EncryptionEngineException;

    /**
     * Encrypts buffer
     *
     * @param data data bytes array
     * @throws EncryptionEngineException
     */
    void encrypt(byte[] data, int offset, int len) throws EncryptionEngineException;

    byte[] getIV();

    /**
     * Sets current iv
     *
     * @param iv current iv
     */
    void setIV(byte[] iv);

    int getIVSize();

    /**
     * Returns current encryption/decryption key
     *
     * @return current encryption/decryption key
     */
    byte[] getKey();

    /**
     * Set encryption/decryption key
     *
     * @param key encryption/decryption key
     */
    void setKey(byte[] key);

    /**
     * Returns encryption/decryption key size
     *
     * @return encryption/decryption key
     */
    int getKeySize();

    void close();

    String getCipherName();

    String getCipherModeName();
}
