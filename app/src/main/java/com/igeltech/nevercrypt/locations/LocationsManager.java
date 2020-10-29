package com.igeltech.nevercrypt.locations;

import android.content.Context;

import com.igeltech.nevercrypt.settings.Settings;

public class LocationsManager extends LocationsManagerBase
{
    public LocationsManager(Context context, Settings settings)
    {
        super(context, settings);
    }

    public static boolean isOpen(Location loc)
    {
        return !(loc instanceof Openable) || ((Openable) loc).isOpen();
    }

    public static boolean isOpenableAndOpen(Location loc)
    {
        return loc instanceof Openable && isOpen(loc);
    }
}
