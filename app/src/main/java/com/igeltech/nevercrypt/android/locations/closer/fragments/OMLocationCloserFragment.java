package com.igeltech.nevercrypt.android.locations.closer.fragments;

import android.content.Context;

import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.Openable;

public class OMLocationCloserFragment extends OpenableLocationCloserFragment
{
    public static void unmountAndClose(Context context, Location location, boolean forceClose) throws Exception
    {
        OpenableLocationCloserFragment.closeLocation(context, (Openable) location, forceClose);
    }
}
