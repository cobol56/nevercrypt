package com.igeltech.nevercrypt.android.navigdrawer;

import com.igeltech.nevercrypt.android.locations.DeviceRootNPLocation;
import com.igeltech.nevercrypt.locations.Location;

import java.util.List;

public class DrawerLocalFilesMenu extends DrawerLocalFilesMenuBase
{
    public DrawerLocalFilesMenu(DrawerControllerBase drawerController)
    {
        super(drawerController);
    }

    @Override
    protected void addLocationMenuItem(List<DrawerMenuItemBase> list, Location loc)
    {
        if(loc instanceof DeviceRootNPLocation && _allowDeviceLocations)
            list.add(new DrawerDeviceRootMemoryItem(loc, getDrawerController()));
        else
            super.addLocationMenuItem(list, loc);
    }

}
