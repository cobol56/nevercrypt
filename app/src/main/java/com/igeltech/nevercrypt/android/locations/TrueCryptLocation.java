package com.igeltech.nevercrypt.android.locations;

import android.content.Context;
import android.net.Uri;

import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.Container;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManagerBase;
import com.igeltech.nevercrypt.settings.Settings;
import com.igeltech.nevercrypt.truecrypt.FormatInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TrueCryptLocation extends ContainerBasedLocation
{
	public static final String URI_SCHEME = "tc";

    public TrueCryptLocation(Uri uri, LocationsManagerBase lm, Context context, Settings settings) throws Exception
    {
        super(uri, lm, context, settings);
    }

	public TrueCryptLocation(Location location, Context context) throws IOException
	{
		this(location, null, context, UserSettings.getSettings(context));
	}

	public TrueCryptLocation(Location containerLocation, Container cont, Context context, Settings settings)
	{
		super(containerLocation, cont, context, settings);
	}

	public TrueCryptLocation(TrueCryptLocation sibling)
	{
		super(sibling);
	}


	@Override
	public Uri getLocationUri()
	{
		return makeUri(URI_SCHEME).build();
	}

	@Override
	public TrueCryptLocation copy()
	{
		return new TrueCryptLocation(this);
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
