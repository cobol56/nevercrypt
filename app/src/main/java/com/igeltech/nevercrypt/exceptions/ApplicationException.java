package com.igeltech.nevercrypt.exceptions;

public class ApplicationException extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int _code;

    public ApplicationException()
    {
    }

    public ApplicationException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public ApplicationException(String msg)
    {
        super(msg);
    }

    public int getCode()
    {
        return _code;
    }

    public void setCode(int code)
    {
        _code = code;
    }
}
