package com.igeltech.nevercrypt.crypto.kdf;

import com.igeltech.nevercrypt.crypto.EncryptionEngineException;

import java.security.NoSuchAlgorithmException;

public class HMACSHA1KDF extends PBKDF
{
    @Override
    protected HMAC initHMAC(byte[] password) throws EncryptionEngineException
    {
        try
        {
            return new HMACSHA1(password);
        }
        catch (NoSuchAlgorithmException e)
        {
            EncryptionEngineException e1 = new EncryptionEngineException();
            e1.initCause(e);
            throw e1;
        }
    }
}