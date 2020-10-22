package com.igeltech.nevercrypt.fs.encfs.codecs.name;

import com.igeltech.nevercrypt.fs.encfs.DataCodecInfo;
import com.igeltech.nevercrypt.fs.encfs.NameCodec;
import com.igeltech.nevercrypt.fs.encfs.ciphers.StreamNameCipher;

public class StreamNameCodecInfo extends NameCodecInfoBase
{
    public static final String NAME = "nameio/stream";

    @Override
    public NameCodec getEncDec()
    {
        DataCodecInfo dci = getConfig().getDataCodecInfo();
        return new StreamNameCipher(dci.getStreamEncDec(), dci.getChecksumCalculator());
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getDescr()
    {
        return "Stream: Stream encoding, keeps filenames as short as possible";
    }

    @Override
    public int getVersion1()
    {
        return 2;
    }

    @Override
    public int getVersion2()
    {
        return 1;
    }

    @Override
    protected NameCodecInfoBase createNew()
    {
        return new StreamNameCodecInfo();
    }
}
