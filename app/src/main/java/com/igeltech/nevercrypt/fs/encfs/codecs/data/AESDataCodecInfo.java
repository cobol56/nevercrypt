package com.igeltech.nevercrypt.fs.encfs.codecs.data;

import com.igeltech.nevercrypt.crypto.EncryptionEngine;
import com.igeltech.nevercrypt.crypto.FileEncryptionEngine;
import com.igeltech.nevercrypt.fs.encfs.AlgInfo;
import com.igeltech.nevercrypt.fs.encfs.Config;
import com.igeltech.nevercrypt.fs.encfs.DataCodecInfo;
import com.igeltech.nevercrypt.fs.encfs.ciphers.AESCBCFileCipher;
import com.igeltech.nevercrypt.fs.encfs.ciphers.AESCFBStreamCipher;
import com.igeltech.nevercrypt.fs.encfs.macs.MACCalculator;
import com.igeltech.nevercrypt.fs.encfs.macs.SHA1MACCalculator;

public class AESDataCodecInfo implements DataCodecInfo
{
    public static final String NAME = "ssl/aes";

    @Override
    public FileEncryptionEngine getFileEncDec()
    {
        return new AESCBCFileCipher(_config.getKeySize(), _config.getBlockSize());
    }

    @Override
    public EncryptionEngine getStreamEncDec()
    {
        return new AESCFBStreamCipher(_config.getKeySize());
    }

    @Override
    public MACCalculator getChecksumCalculator()
    {
        return new SHA1MACCalculator(_config.getKeySize());
    }

    @Override
    public AlgInfo select(Config config)
    {
        AESDataCodecInfo info = new AESDataCodecInfo();
        info._config = config;
        return info;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getDescr()
    {
        return "AES: 16 byte block cipher";
    }

    @Override
    public int getVersion1()
    {
        return 3;
    }

    @Override
    public int getVersion2()
    {
        return 0;
    }

    private Config _config;
}
