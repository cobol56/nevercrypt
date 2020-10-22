package com.igeltech.nevercrypt.android.errors;

import android.content.Context;

import com.igeltech.nevercrypt.android.R;

public class ClosedContainerException extends UserException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4482745672661567457L;

	public ClosedContainerException(Context context)
	{
		super(context,R.string.err_target_container_is_closed);
	}

}
