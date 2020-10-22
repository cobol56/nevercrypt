package com.igeltech.nevercrypt.crypto;

public interface BlockCipherNative extends BlockCipher
{	
	long getNativeInterfacePointer() throws EncryptionEngineException;
}
