package com.igeltech.nevercrypt.android.navigdrawer;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.DocumentTreeLocation;

public class DrawerManageLocalStorages extends DrawerManageLocationMenuItem
{
    public DrawerManageLocalStorages(DrawerControllerBase drawerController)
    {
        super(drawerController);
    }

    @Override
    protected String getLocationType()
    {
        return DocumentTreeLocation.URI_SCHEME;
    }

    @Override
    public String getTitle()
    {
        return getContext().getString(R.string.manage_local_storages);
    }
}
