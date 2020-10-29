package com.igeltech.nevercrypt.crypto.engines;

import com.igeltech.nevercrypt.crypto.BlockCipherNative;
import com.igeltech.nevercrypt.crypto.CipherFactory;
import com.igeltech.nevercrypt.crypto.blockciphers.GOST;
import com.igeltech.nevercrypt.crypto.modes.CBC;

public class GOSTCBC extends CBC
{
    public GOSTCBC()
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
                return new GOST();
            }
        });
    }

    @Override
    public String getCipherName()
    {
        return "gost";
    }

    @Override
    public int getKeySize()
    {
        return 32;
    }
}

    