package com.igeltech.nevercrypt.veracrypt;

import com.igeltech.nevercrypt.container.VolumeLayout;


public class FormatInfo extends com.igeltech.nevercrypt.truecrypt.FormatInfo
{

	public static final String FORMAT_NAME = "VeraCrypt";

	@Override
	public String getFormatName()
	{
		return FORMAT_NAME;
	}

	@Override
	public VolumeLayout getVolumeLayout()
	{
		return new com.igeltech.nevercrypt.veracrypt.VolumeLayout();
	}

	@Override
	public int getOpeningPriority()
	{		
		return 3;
	}

	@Override
	public boolean hasCustomKDFIterationsSupport()
	{
		return true;
	}

}
