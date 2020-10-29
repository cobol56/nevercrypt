package com.igeltech.nevercrypt.crypto.engines;

import com.igeltech.nevercrypt.crypto.BlockCipherNative;
import com.igeltech.nevercrypt.crypto.CipherFactory;
import com.igeltech.nevercrypt.crypto.blockciphers.Serpent;
import com.igeltech.nevercrypt.crypto.modes.XTS;

public class SerpentXTS extends XTS
{
    public SerpentXTS()
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
                return new Serpent();
            }
        });
    }

    @Override
    public int getKeySize()
    {
        return 2 * 32;
    }

    @Override
    public String getCipherName()
    {
        return "serpent";
    }
}

    