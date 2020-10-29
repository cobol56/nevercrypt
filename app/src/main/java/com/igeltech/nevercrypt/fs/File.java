package com.igeltech.nevercrypt.fs;

import android.os.ParcelFileDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface File extends FSRecord
{
    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    RandomAccessIO getRandomAccessIO(AccessMode accessMode) throws IOException;

    long getSize() throws IOException;

    ParcelFileDescriptor getFileDescriptor(AccessMode accessMode) throws IOException;

    void copyToOutputStream(OutputStream output, long offset, long count, ProgressInfo progressInfo) throws IOException;

    void copyFromInputStream(InputStream input, long offset, long count, ProgressInfo progressInfo) throws IOException;

    enum AccessMode
    {
        Read, Write, WriteAppend, ReadWrite, ReadWriteTruncate
    }

    interface ProgressInfo
    {
        void setProcessed(long num);

        boolean isCancelled();
    }
}
