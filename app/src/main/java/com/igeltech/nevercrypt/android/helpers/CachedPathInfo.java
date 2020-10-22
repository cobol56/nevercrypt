package com.igeltech.nevercrypt.android.helpers;

import com.igeltech.nevercrypt.fs.Path;

import java.io.IOException;
import java.util.Date;

public interface CachedPathInfo
{	
	Path getPath();
	String getPathDesc();
	String getName();
	boolean isFile();
	boolean isDirectory();	
	Date getModificationDate();
	long getSize();
	void init(Path path) throws IOException;
}
