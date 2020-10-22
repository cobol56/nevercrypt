package com.igeltech.nevercrypt.exceptions;

public class WrongPasswordException extends ApplicationException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongPasswordException()
	{
		super("Wrong password");
	}

	public WrongPasswordException(String msg)
	{
		super(msg);
	}
}
