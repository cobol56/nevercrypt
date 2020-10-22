package com.igeltech.nevercrypt.android.settings;

import com.igeltech.nevercrypt.locations.Location;

public interface PropertiesHostWithLocation extends PropertyEditor.Host
{
    Location getTargetLocation();
}
