package com.igeltech.nevercrypt.util;

import com.igeltech.nevercrypt.util.exec.ExternalProgramFailedException;

import java.io.IOException;

public interface TextShell
{
	void executeCommand(Object... args) throws IOException;
	void writeStdInput(String data) throws IOException;
	String waitResult() throws ExternalProgramFailedException, IOException;
	void close();
}
