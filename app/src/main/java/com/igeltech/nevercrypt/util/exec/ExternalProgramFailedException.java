package com.igeltech.nevercrypt.util.exec;

import com.igeltech.nevercrypt.exceptions.ApplicationException;

public class ExternalProgramFailedException extends ApplicationException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final int _exitCode;
    private final String _output;

    public ExternalProgramFailedException(int exitCode, String output, String... command)
    {
        super(getMsg(exitCode, output, command));
        //_command = command;
        _exitCode = exitCode;
        _output = output;
    }
    //	public String getCommand()
    //	{
    //		return _command;
    //	}

    private static String getMsg(int exitCode, String output, String... command)
    {
        String tmp = "";
        for (Object s : command)
            tmp += s.toString() + ' ';
        return String.format("External program failed.\nCommand: %s\nExit code: %d\nOutput: %s", tmp, exitCode, output);
    }

    public int getExitCode()
    {
        return _exitCode;
    }

    public String getCommandOutput()
    {
        return _output;
    }
    //private final String _command;
}
