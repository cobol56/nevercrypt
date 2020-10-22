package com.sovworks.eds.crypto.kdf;

import com.sovworks.eds.crypto.EncryptionEngineException;

import java.security.NoSuchAlgorithmException;

public class HMACRIPEMD160KDF extends PBKDF
{
	@Override
	protected HMAC initHMAC(byte[] password) throws EncryptionEngineException
	{
		try
		{
			return new HMACRIPEMD160(password);
		}
		catch (NoSuchAlgorithmException e)
		{
			EncryptionEngineException e1 = new EncryptionEngineException();
			e1.initCause(e);
			throw e1;
		}
	}	
	
	@Override
	protected int getDefaultIterationsCount()
	{		
		return 2000;
	}
}