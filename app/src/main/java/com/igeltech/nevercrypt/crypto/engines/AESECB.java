package com.igeltech.nevercrypt.crypto.engines;

import com.igeltech.nevercrypt.crypto.blockciphers.AES;
import com.igeltech.nevercrypt.crypto.modes.ECB;


public class AESECB extends ECB
{
	public AESECB()
	{
		this(32);
	}

	public AESECB(final int keySize)
	{
		super(new AES(keySize));
	}	
	
	@Override
	public String getCipherName()
	{
		return "aes";
	}
}

    