package com.igeltech.nevercrypt.crypto;

public interface CipherFactory
{
	BlockCipherNative createCipher(int typeIndex);
	int getNumberOfCiphers();
}
