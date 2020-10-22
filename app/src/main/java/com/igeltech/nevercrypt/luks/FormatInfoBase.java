package com.igeltech.nevercrypt.luks;

import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.VolumeLayout;
import com.igeltech.nevercrypt.crypto.EncryptedFile;
import com.igeltech.nevercrypt.crypto.EncryptedFileWithCache;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.FileSystemInfo;
import com.igeltech.nevercrypt.fs.RandomAccessIO;

import java.io.IOException;
import java.security.SecureRandom;


public abstract class FormatInfoBase implements ContainerFormatInfo
{
	public static final String FORMAT_NAME = "LUKS";
	public static final int MAX_PASSWORD_LENGTH = 8192*1000;

	@Override
	public String getFormatName()
	{
		return FORMAT_NAME;
	}

	@Override
	public VolumeLayout getVolumeLayout()
	{
		return new com.igeltech.nevercrypt.luks.VolumeLayout();
	}

	@Override
	public boolean hasHiddenContainerSupport()
	{
		return false;
	}
	
	@Override
	public boolean hasKeyfilesSupport()
	{
		return false;
	}

	@Override
	public boolean hasCustomKDFIterationsSupport()
	{
		return false;
	}

	@Override
	public VolumeLayout getHiddenVolumeLayout()
	{
		return null;
	}
	
	@Override
	public int getOpeningPriority()
	{		
		return 1;
	}

	@Override
	public int getMaxPasswordLength()
	{
		return MAX_PASSWORD_LENGTH;
	}

	@Override
	public String toString()
	{
		return getFormatName();
	}

	@Override
	public void formatContainer(RandomAccessIO io, VolumeLayout layout, FileSystemInfo fsInfo) throws IOException, ApplicationException
	{
		com.igeltech.nevercrypt.luks.VolumeLayout vl = ((com.igeltech.nevercrypt.luks.VolumeLayout)layout);
		long len = vl.getEncryptedDataSize(io.length()) + vl.getEncryptedDataOffset();
		io.setLength(len);
		SecureRandom sr = new SecureRandom();
		prepareHeaderLocations(sr, io, vl);
		vl.writeHeader(io);
		EncryptedFile et = new EncryptedFileWithCache(io, vl);
		vl.formatFS(et, fsInfo);
		et.close(false);				
	}
	
	protected void prepareHeaderLocations(SecureRandom sr, RandomAccessIO io, com.igeltech.nevercrypt.luks.VolumeLayout layout) throws IOException
	{
		writeRandomData(sr, io, 0, layout.getEncryptedDataOffset());
	}
	
	protected void writeRandomData(SecureRandom sr, RandomAccessIO io, long start, long length) throws IOException
	{
		io.seek(start);
		byte[] tbuf = new byte[8*512];
		for(int i=0;i<length;i+=tbuf.length)
		{
			sr.nextBytes(tbuf);
			io.write(tbuf,0,tbuf.length);
		}
	}

	
	
}
