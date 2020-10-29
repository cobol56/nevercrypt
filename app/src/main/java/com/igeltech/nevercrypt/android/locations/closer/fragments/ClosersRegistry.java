package com.igeltech.nevercrypt.android.locations.closer.fragments;

import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.Openable;

public class ClosersRegistry
{
    public static LocationCloserBaseFragment getDefaultCloserForLocation(Location location)
    {
        return location instanceof Openable ? new OpenableLocationCloserFragment() : new LocationCloserBaseFragment();
    }
}
