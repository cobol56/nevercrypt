package com.igeltech.nevercrypt.fs.errors;

import java.io.IOException;

public class DirectoryIsNotEmptyException extends IOException
{
    private static final long serialVersionUID = 1L;
    private final String _path;

    public DirectoryIsNotEmptyException(String path)
    {
        _path = path;
    }

    public DirectoryIsNotEmptyException(String path, String msg)
    {
        super(msg);
        _path = path;
    }

    public String getPath()
    {
        return _path;
    }
}
