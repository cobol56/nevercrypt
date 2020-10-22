package com.igeltech.nevercrypt.container;


import com.igeltech.nevercrypt.crypto.FileEncryptionEngine;

import java.io.Closeable;

public interface EncryptedFileLayout extends Closeable
{
    long getEncryptedDataOffset();

    long getEncryptedDataSize(long fileSize);

    FileEncryptionEngine getEngine();

    void setEncryptionEngineIV(FileEncryptionEngine eng, long decryptedVolumeOffset);
}    