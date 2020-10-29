package com.igeltech.nevercrypt.android.navigdrawer;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.ContainerBasedLocation;

public class DrawerManageContainersMenuItem extends DrawerManageLocationMenuItem
{
    public DrawerManageContainersMenuItem(DrawerControllerBase drawerController)
    {
        super(drawerController);
    }

    @Override
    protected String getLocationType()
    {
        return ContainerBasedLocation.URI_SCHEME;
    }

    @Override
    public String getTitle()
    {
        return getContext().getString(R.string.manage_containers);
    }
}
