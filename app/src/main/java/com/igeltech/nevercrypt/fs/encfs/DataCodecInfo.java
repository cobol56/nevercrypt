package com.igeltech.nevercrypt.fs.encfs;

import com.igeltech.nevercrypt.crypto.EncryptionEngine;
import com.igeltech.nevercrypt.crypto.FileEncryptionEngine;
import com.igeltech.nevercrypt.fs.encfs.macs.MACCalculator;

public interface DataCodecInfo extends AlgInfo
{
    FileEncryptionEngine getFileEncDec();

    EncryptionEngine getStreamEncDec();

    MACCalculator getChecksumCalculator();
}
