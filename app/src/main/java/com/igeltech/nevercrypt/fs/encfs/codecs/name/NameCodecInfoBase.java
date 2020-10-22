package com.igeltech.nevercrypt.fs.encfs.codecs.name;

import com.igeltech.nevercrypt.fs.encfs.AlgInfo;
import com.igeltech.nevercrypt.fs.encfs.Config;
import com.igeltech.nevercrypt.fs.encfs.NameCodecInfo;

public abstract class NameCodecInfoBase implements NameCodecInfo
{
    @Override
    public boolean useChainedNamingIV()
    {
        return _config.useChainedNameIV();
    }

    @Override
    public AlgInfo select(Config config)
    {
        NameCodecInfoBase info = createNew();
        info._config = config;
        return info;
    }

    public Config getConfig()
    {
        return _config;
    }

    private Config _config;

    protected abstract NameCodecInfoBase createNew();
}
