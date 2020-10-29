package com.igeltech.nevercrypt.fs.errors;

import java.io.IOException;

public class WrongImageFormatException extends IOException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public WrongImageFormatException(String msg)
    {
        super(msg);
    }
}
