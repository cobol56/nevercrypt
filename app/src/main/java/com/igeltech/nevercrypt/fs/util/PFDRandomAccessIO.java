package com.igeltech.nevercrypt.fs.util;

import android.os.ParcelFileDescriptor;

import java.io.IOException;

public class PFDRandomAccessIO extends FDRandomAccessIO
{
    private final ParcelFileDescriptor _pfd;

    public PFDRandomAccessIO(ParcelFileDescriptor pfd)
    {
        super(pfd.getFd());
        _pfd = pfd;
    }

    @Override
    public void close() throws IOException
    {
        _pfd.close();
        super.close();
    }
}
