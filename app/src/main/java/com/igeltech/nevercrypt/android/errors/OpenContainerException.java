package com.igeltech.nevercrypt.android.errors;

import android.content.Context;

import com.igeltech.nevercrypt.android.R;

public class OpenContainerException extends UserException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public OpenContainerException(Context context)
    {
        super(context, R.string.err_failed_opening_container);
    }
}
