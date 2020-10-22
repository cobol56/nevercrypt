package com.igeltech.nevercrypt.locations;

import com.igeltech.nevercrypt.android.helpers.ProgressReporter;
import com.igeltech.nevercrypt.crypto.SecureBuffer;

import java.io.IOException;

public interface Openable extends Location
{
	String PARAM_PASSWORD = "com.igeltech.nevercrypt.android.PASSWORD";
	String PARAM_KDF_ITERATIONS = "com.igeltech.nevercrypt.android.KDF_ITERATIONS";

	void setPassword(SecureBuffer pass);
	boolean hasPassword();
	boolean requirePassword();
	boolean hasCustomKDFIterations();
	boolean requireCustomKDFIterations();
	void setNumKDFIterations(int num);
	void setOpenReadOnly(boolean readOnly);
	boolean isOpen();
	void open() throws Exception;	
	void close(boolean force) throws IOException;
	void setOpeningProgressReporter(ProgressReporter pr);
}
