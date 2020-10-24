package com.igeltech.nevercrypt.android.locations;

import com.igeltech.nevercrypt.android.locations.opener.fragments.ContainerOpenerFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.EncFSOpenerFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.LocationOpenerBaseFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.LocationOpenerFragment;
import com.igeltech.nevercrypt.locations.ContainerLocation;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.Openable;

public class OpenersRegistry
{
    public static LocationOpenerBaseFragment getDefaultOpenerForLocation(Location location)
    {
        if(location instanceof ContainerLocation)
            return new ContainerOpenerFragment();
        if(location instanceof EncFsLocationBase)
            return new EncFSOpenerFragment();
        if(location instanceof CryptoLocation)
            return new LocationOpenerFragment();
        if(location instanceof Openable)
            return new LocationOpenerFragment();
        return new LocationOpenerBaseFragment();
    }
}
