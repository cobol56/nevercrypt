package com.igeltech.nevercrypt.crypto.engines;

import com.igeltech.nevercrypt.crypto.BlockCipherNative;
import com.igeltech.nevercrypt.crypto.CipherFactory;
import com.igeltech.nevercrypt.crypto.blockciphers.Twofish;
import com.igeltech.nevercrypt.crypto.modes.CBC;

public class TwofishCBC extends CBC
{
    public TwofishCBC()
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
                return new Twofish();
            }
        });
    }

    @Override
    public String getCipherName()
    {
        return "twofish";
    }

    @Override
    public int getKeySize()
    {
        return 32;
    }
}

    