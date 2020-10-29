package com.igeltech.nevercrypt.container;

import android.os.Parcel;

public abstract class LocationFormatter extends LocationFormatterBase
{
    public LocationFormatter()
    {

    }

    protected LocationFormatter(Parcel in)
    {
        super(in);
    }
}
