package com.igeltech.nevercrypt.container;

import com.igeltech.nevercrypt.android.helpers.ContainerOpeningProgressReporter;
import com.igeltech.nevercrypt.crypto.FileEncryptionEngine;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.FileSystemInfo;
import com.igeltech.nevercrypt.fs.RandomAccessIO;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

public interface VolumeLayout extends EncryptedFileLayout
{
    void initNew();

    boolean readHeader(RandomAccessIO input) throws IOException, ApplicationException;

    void writeHeader(RandomAccessIO output) throws IOException, ApplicationException;

    void formatFS(RandomAccessIO output, FileSystemInfo fsInfo) throws ApplicationException, IOException;

    void setEngine(FileEncryptionEngine enc);

    MessageDigest getHashFunc();

    void setHashFunc(MessageDigest hf);

    void setPassword(byte[] password);

    void setNumKDFIterations(int num);

    List<FileEncryptionEngine> getSupportedEncryptionEngines();

    List<MessageDigest> getSupportedHashFuncs();

    void setOpeningProgressReporter(ContainerOpeningProgressReporter reporter);
}    