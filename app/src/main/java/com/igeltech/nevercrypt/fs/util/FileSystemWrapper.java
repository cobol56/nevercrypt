package com.igeltech.nevercrypt.fs.util;

import com.igeltech.nevercrypt.fs.FileSystem;

import java.io.IOException;

public abstract class FileSystemWrapper implements FileSystem
{
    private final FileSystem _base;

    public FileSystemWrapper(FileSystem base)
    {
        _base = base;
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof FileSystemWrapper ? equals(((FileSystemWrapper) o).getBase()) : _base.equals(o);
    }

    @Override
    public int hashCode()
    {
        return _base.hashCode();
    }

    public FileSystem getBase()
    {
        return _base;
    }

    @Override
    public void close(boolean force) throws IOException
    {
        getBase().close(force);
    }

    @Override
    public boolean isClosed()
    {
        return getBase().isClosed();
    }
}
