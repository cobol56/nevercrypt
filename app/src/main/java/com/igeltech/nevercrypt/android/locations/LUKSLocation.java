package com.igeltech.nevercrypt.android.locations;

import android.content.Context;
import android.net.Uri;

import com.igeltech.nevercrypt.container.EdsContainer;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManagerBase;
import com.igeltech.nevercrypt.settings.Settings;

public class LUKSLocation extends LUKSLocationBase
{
	public LUKSLocation(Uri uri, LocationsManagerBase lm, Context context, Settings settings) throws Exception
    {
        super(uri, lm, context, settings);
    }

	public LUKSLocation(Location containerLocation, EdsContainer cont, Context context, Settings settings)
	{
		super(containerLocation, cont, context, settings);
	}

	private LUKSLocation(LUKSLocation sibling)
	{
		super(sibling);
	}

	@Override
	public LUKSLocation copy()
	{
		return new LUKSLocation(this);
	}

}
