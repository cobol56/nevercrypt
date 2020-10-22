package com.sovworks.eds.exceptions;

public class WrongFileFormatException extends ApplicationException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongFileFormatException()
	{
		super("Wrong password or unsupported container format");
	}

	public WrongFileFormatException(String msg)
	{
		super(msg);
	}
}
