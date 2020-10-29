package com.igeltech.nevercrypt.crypto.kdf;

import com.igeltech.nevercrypt.crypto.EncryptionEngineException;

import java.security.MessageDigest;

public class HashBasedPBKDF2 extends PBKDF
{
    public HashBasedPBKDF2(MessageDigest md)
    {
        this(md, guessMDBlockSize(md));
    }

    public HashBasedPBKDF2(MessageDigest md, int blockSize)
    {
        _md = md;
        _blockSize = blockSize;
    }

    @Override
    protected HMAC initHMAC(byte[] password) throws EncryptionEngineException
    {
        _md.reset();
        return new HMAC(password, _md, _blockSize);
    }

    private final MessageDigest _md;
    private final int _blockSize;

    private static int guessMDBlockSize(MessageDigest md)
    {
        String mdn = md.getAlgorithm().toLowerCase();
        if (mdn.equals("sha-512") || mdn.equals("sha512"))
            return 128;
        return 64;
		/*
		if(mdn.equals("md5") 
				|| mdn.equals("sha-0") 
				|| mdn.equals("sha-1") 
				|| mdn.equals("sha-224") 
				|| mdn.equals("sha-256")
				|| mdn.equals("whirlpool")
				|| mdn.equals("ripemd160"))
			return 64;*/

    }
}
