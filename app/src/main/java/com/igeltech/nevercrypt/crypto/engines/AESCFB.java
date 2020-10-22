package com.igeltech.nevercrypt.crypto.engines;

import com.igeltech.nevercrypt.crypto.BlockCipherNative;
import com.igeltech.nevercrypt.crypto.CipherFactory;
import com.igeltech.nevercrypt.crypto.blockciphers.AES;
import com.igeltech.nevercrypt.crypto.modes.CFB;


public class AESCFB extends CFB
{
	public AESCFB(final int keySize)
	{
		super(new CipherFactory()
		{
			
			@Override
			public int getNumberOfCiphers()
			{
				return 1;
			}
			
			@Override
			public BlockCipherNative createCipher(int typeIndex)
			{
				return new AES(keySize);
			}
		});
		_keySize = keySize;
	}	
	
	@Override
	public String getCipherName()
	{
		return "aes";
	}
	

    @Override
	public int getKeySize()
	{
    	return _keySize;
	}

	private final int _keySize;
}

    