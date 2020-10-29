package com.igeltech.nevercrypt.android.locations;

import android.content.Context;
import android.net.Uri;

import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.fs.encfs.FS;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManagerBase;
import com.igeltech.nevercrypt.settings.Settings;

import java.io.IOException;

public class EncFsLocation extends EncFsLocationBase
{
    public EncFsLocation(Uri uri, LocationsManagerBase lm, Context context, Settings settings) throws Exception
    {
        super(uri, lm, context, settings);
    }

    public EncFsLocation(Location location, Context context) throws IOException
    {
        this(location, null, context, UserSettings.getSettings(context));
    }

    public EncFsLocation(Location containerLocation, FS encFs, Context context, Settings settings)
    {
        super(containerLocation, encFs, context, settings);
    }

    public EncFsLocation(EncFsLocationBase sibling)
    {
        super(sibling);
    }

    @Override
    public EncFsLocation copy()
    {
        return new EncFsLocation(this);
    }
}
