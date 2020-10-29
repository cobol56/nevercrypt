package com.igeltech.nevercrypt.fs.errors;

import java.io.IOException;

public class NoFreeSpaceLeftException extends IOException
{
    private static final long serialVersionUID = 1L;

    public NoFreeSpaceLeftException()
    {
        super("No free space left");
    }
}
