package com.igeltech.nevercrypt.android.errors;

import android.content.Context;

import com.igeltech.nevercrypt.android.R;

public class NoFreeSpaceLeftException extends UserException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NoFreeSpaceLeftException(Context context)
    {
        super(context, R.string.no_free_space_left);
    }
}