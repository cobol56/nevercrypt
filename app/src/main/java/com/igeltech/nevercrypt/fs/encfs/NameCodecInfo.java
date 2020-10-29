package com.igeltech.nevercrypt.fs.encfs;

public interface NameCodecInfo extends AlgInfo
{
    NameCodec getEncDec();

    boolean useChainedNamingIV();
}
