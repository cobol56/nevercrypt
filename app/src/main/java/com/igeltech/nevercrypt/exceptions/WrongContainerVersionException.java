package com.igeltech.nevercrypt.exceptions;


public class WrongContainerVersionException extends ApplicationException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongContainerVersionException()
	{
		super("Unsupported container version");
	}

	public WrongContainerVersionException(String msg)
	{
		super(msg);
	}

}
