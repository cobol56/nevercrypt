package com.igeltech.nevercrypt.android.errors;

import android.content.Context;

import com.igeltech.nevercrypt.android.R;

public class ExternalStorageNotAvailableException extends UserException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExternalStorageNotAvailableException(Context context)
	{
		super(context,R.string.err_external_storage_is_not_available);
	}

}
