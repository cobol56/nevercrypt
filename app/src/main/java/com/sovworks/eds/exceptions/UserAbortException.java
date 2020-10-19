package com.sovworks.eds.exceptions;


public class UserAbortException extends ApplicationException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAbortException()
	{
	}

	public UserAbortException(String msg)
	{
		super(msg);
	}
}
