package com.igeltech.nevercrypt.fs.encfs;

public interface AlgInfo
{
    AlgInfo select(Config config);

    String getName();

    String getDescr();

    int getVersion1();

    int getVersion2();
}
