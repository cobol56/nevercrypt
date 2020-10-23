package com.igeltech.nevercrypt.android.locations;

import android.content.Context;
import android.net.Uri;

import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.Container;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManagerBase;
import com.igeltech.nevercrypt.luks.FormatInfo;
import com.igeltech.nevercrypt.settings.Settings;

import java.util.Collections;
import java.util.List;

abstract class LUKSLocationBase extends ContainerBasedLocation
{
	public static final String URI_SCHEME = "luks";

    LUKSLocationBase(Uri uri, LocationsManagerBase lm, Context context, Settings settings) throws Exception
    {
        super(uri, lm, context, settings);
    }

	LUKSLocationBase(Location containerLocation, Container cont, Context context, Settings settings)
	{
		super(containerLocation, cont, context, settings);
	}

	LUKSLocationBase(LUKSLocationBase sibling)
	{
		super(sibling);
	}


	@Override
	public Uri getLocationUri()
	{
		return makeUri(URI_SCHEME).build();
	}


	@Override
	public List<ContainerFormatInfo> getSupportedFormats()
	{
		return Collections.singletonList(getContainerFormatInfo());
	}

	@Override
	public ContainerFormatInfo getContainerFormatInfo()
	{
		return new FormatInfo();
	}

}
