package com.igeltech.nevercrypt.android.errors;

import android.content.Context;

import com.igeltech.nevercrypt.android.R;

public class InputOutputException extends UserException
{	

	public InputOutputException(Context context)
	{
		super(context,R.string.err_input_output);
	}
	
	public InputOutputException(Context context,Throwable cause)
	{
		super(context,R.string.err_input_output,cause);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
