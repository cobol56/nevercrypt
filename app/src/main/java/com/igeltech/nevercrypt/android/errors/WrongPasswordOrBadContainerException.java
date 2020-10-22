package com.igeltech.nevercrypt.android.errors;

import android.content.Context;

import com.igeltech.nevercrypt.android.R;

public class WrongPasswordOrBadContainerException extends UserException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongPasswordOrBadContainerException(Context context)
	{
		super(context,R.string.bad_container_file_or_wrong_password);
	}
}
