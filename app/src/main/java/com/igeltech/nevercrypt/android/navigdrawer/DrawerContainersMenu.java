package com.igeltech.nevercrypt.android.navigdrawer;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.EncFsLocationBase;
import com.igeltech.nevercrypt.locations.ContainerLocation;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.util.ArrayList;
import java.util.Collection;

public class DrawerContainersMenu extends DrawerSubMenuBase
{
    @Override
    public String getTitle()
    {
        return getContext().getString(R.string.containers);
    }

    public DrawerContainersMenu(DrawerControllerBase drawerController)
    {
        super(drawerController);
    }

    @Override
    protected Collection<DrawerMenuItemBase> getSubItems()
    {
        LocationsManager lm = LocationsManager.getLocationsManager(getContext());
        ArrayList<DrawerMenuItemBase> res = new ArrayList<>();
        for(CryptoLocation loc: lm.getLoadedCryptoLocations(true))
        {
            if(loc instanceof ContainerLocation)
                res.add(new DrawerContainerMenuItem(loc, getDrawerController()));
            else if(loc instanceof EncFsLocationBase)
                res.add(new DrawerEncFsMenuItem(loc, getDrawerController()));
        }
        res.add(new DrawerManageContainersMenuItem(getDrawerController()));

        return res;
    }
}
