package com.igeltech.nevercrypt.fs.util;

import com.igeltech.nevercrypt.fs.DataOutput;
import com.igeltech.nevercrypt.fs.RandomAccessIO;
import com.igeltech.nevercrypt.fs.RandomStorageAccess;

import java.io.IOException;
import java.io.OutputStream;

public class RandomAccessOutputStream extends OutputStream implements DataOutput, RandomStorageAccess
{
    public RandomAccessOutputStream(RandomAccessIO io) throws IOException
    {
        _io = io;
    }

    @Override
    public void close() throws IOException
    {
        _io.close();
    }

    public void seek(long position) throws IOException
    {
        _io.seek(position);
    }

    public long getFilePointer() throws IOException
    {
        return _io.getFilePointer();
    }

    public void write(int data) throws IOException
    {
        _io.write(data);
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        _io.write(b, off, len);
    }

    @Override
    public long length() throws IOException
    {
        return _io.length();
    }

    private final RandomAccessIO _io;
}
