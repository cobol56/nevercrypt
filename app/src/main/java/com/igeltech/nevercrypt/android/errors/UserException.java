package com.igeltech.nevercrypt.android.errors;

import android.content.Context;

public class UserException extends Exception
{
    private static final long serialVersionUID = 1L;
    private final int _messageId;
    private final Object[] _args;
    private Context _context;

    public UserException(Context context, int messageId)
    {
        super(context != null ? context.getString(messageId) : "");
        _messageId = messageId;
        _args = null;
    }

    public UserException(Context context, int messageId, Throwable cause)
    {
        super(context.getString(messageId), cause);
        _messageId = messageId;
        _args = null;
    }

    public UserException(String defaultMessage, int messageId, Object... args)
    {
        super(defaultMessage);
        _messageId = messageId;
        _args = args;
    }

    protected UserException(String message)
    {
        super(message);
        _messageId = 0;
        _args = null;
    }

    protected UserException()
    {
        _messageId = 0;
        _args = null;
    }

    public void setContext(Context context)
    {
        _context = context;
    }

    @Override
    public String getLocalizedMessage()
    {
        if (_messageId != 0 && _context != null)
        {
            Object[] args = _args == null ? new Object[0] : _args;
            return _context.getString(_messageId, args);
        }
        return super.getLocalizedMessage();
    }
}
