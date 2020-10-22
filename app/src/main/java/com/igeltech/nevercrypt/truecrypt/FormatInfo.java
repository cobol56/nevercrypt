package com.igeltech.nevercrypt.truecrypt;

import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.VolumeLayout;
import com.igeltech.nevercrypt.crypto.EncryptedFileWithCache;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.FileSystemInfo;
import com.igeltech.nevercrypt.fs.RandomAccessIO;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;


public class FormatInfo implements ContainerFormatInfo
{
	public static final String FORMAT_NAME = "TrueCrypt";

	@Override
	public String getFormatName()
	{
		return FORMAT_NAME;
	}

	@Override
	public VolumeLayout getVolumeLayout()
	{
		return new StdLayout();
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
	public int getMaxPasswordLength()
	{
		return 64;
	}

	@Override
	public VolumeLayout getHiddenVolumeLayout()
	{
		return null;
	}
	
	@Override
	public int getOpeningPriority()
	{		
		return 3;
	}

	@Override
	public void formatContainer(RandomAccessIO io, VolumeLayout layout, FileSystemInfo fsType) throws IOException, ApplicationException
	{
		StdLayout tcLayout = ((StdLayout)layout);
		io.seek(tcLayout.getEncryptedDataSize(io.length()) + tcLayout.getEncryptedDataOffset() - 1);
		io.write(0);
		prepareHeaderLocations(getRandom(), io, tcLayout);
		tcLayout.writeHeader(io);
		RandomAccessIO et = getEncryptedRandomAccessIO(io,tcLayout);
		tcLayout.formatFS(et, fsType);
		et.close();
	}
	
	@Override
	public String toString()
	{
		return getFormatName();
	}

	protected Random getRandom()
	{
		return new SecureRandom();
	}

    protected RandomAccessIO getEncryptedRandomAccessIO(RandomAccessIO base, VolumeLayout layout) throws IOException
    {
        return new EncryptedFileWithCache(base, layout)
        {
            @Override
            public void close() throws IOException
            {
                close(false);
            }
        };
    }
	
	protected void prepareHeaderLocations(Random sr, RandomAccessIO io, StdLayout layout) throws IOException
	{
		writeRandomData(sr, io, layout.getHeaderOffset(), getReservedHeadersSpace(layout));
	}
	
	protected void writeRandomData(Random sr, RandomAccessIO io, long start, long length) throws IOException
	{
		io.seek(start);
		byte[] tbuf = new byte[8*512];
		for(int i=0;i<length;i+=tbuf.length)
		{
			sr.nextBytes(tbuf);
			io.write(tbuf,0,tbuf.length);
		}
	}
	
	protected int getReservedHeadersSpace(StdLayout layout)
	{
		return 2*layout.getHeaderSize();
	}
}
