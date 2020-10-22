package com.igeltech.nevercrypt.fs.encfs.ciphers;

import com.igeltech.nevercrypt.crypto.engines.AESCFB;

public class AESCFBStreamCipher extends StreamCipherBase
{
    public AESCFBStreamCipher(int keySize)
    {
        super(new AESCFB(keySize));
    }
}
