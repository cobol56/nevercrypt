package com.igeltech.nevercrypt.android.locations;

import android.content.Context;
import android.net.Uri;

import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.container.Container;
import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManagerBase;
import com.igeltech.nevercrypt.settings.Settings;
import com.igeltech.nevercrypt.veracrypt.FormatInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class VeraCryptLocation extends ContainerBasedLocation
{
    public static final String URI_SCHEME = "vc";

    public VeraCryptLocation(Uri uri, LocationsManagerBase lm, Context context, Settings settings) throws Exception
    {
        super(uri, lm, context, settings);
    }

    public VeraCryptLocation(Location location, Context context) throws IOException
    {
        this(location, null, context, UserSettings.getSettings(context));
    }

    public VeraCryptLocation(Location containerLocation, Container cont, Context context, Settings settings)
    {
        super(containerLocation, cont, context, settings);
    }

    public VeraCryptLocation(VeraCryptLocation sibling)
    {
        super(sibling);
    }

    @Override
    public List<ContainerFormatInfo> getSupportedFormats()
    {
        return Collections.singletonList(getContainerFormatInfo());
    }

    @Override
    public Uri getLocationUri()
    {
        return makeUri(URI_SCHEME).build();
    }

    @Override
    public VeraCryptLocation copy()
    {
        return new VeraCryptLocation(this);
    }

    @Override
    public ContainerFormatInfo getContainerFormatInfo()
    {
        return new FormatInfo();
    }
}
