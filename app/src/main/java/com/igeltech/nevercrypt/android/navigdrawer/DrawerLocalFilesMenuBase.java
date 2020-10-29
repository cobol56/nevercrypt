package com.igeltech.nevercrypt.android.navigdrawer;

import android.content.Intent;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.locations.DocumentTreeLocation;
import com.igeltech.nevercrypt.android.locations.ExternalStorageLocation;
import com.igeltech.nevercrypt.android.locations.InternalSDLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DrawerLocalFilesMenuBase extends DrawerSubMenuBase
{
    @Override
    public String getTitle()
    {
        return getContext().getString(R.string.local_files);
    }

    public DrawerLocalFilesMenuBase(DrawerControllerBase drawerController)
    {
        super(drawerController);
        Intent i = getDrawerController().getMainActivity().getIntent();
        _allowDeviceLocations = i.getBooleanExtra(FileManagerActivity.EXTRA_ALLOW_BROWSE_DEVICE, true);
        _allowDocumentTree = i.getBooleanExtra(FileManagerActivity.EXTRA_ALLOW_BROWSE_DOCUMENT_PROVIDERS, true);
    }

    @Override
    protected Collection<DrawerMenuItemBase> getSubItems()
    {
        ArrayList<DrawerMenuItemBase> res = new ArrayList<>();
        FileManagerActivity act = getDrawerController().getMainActivity();
        Intent i = act.getIntent();
        for (Location loc : LocationsManager.getLocationsManager(act).getLoadedLocations(true))
            addLocationMenuItem(res, loc);

        if (act.isSelectAction() && i.getBooleanExtra(FileManagerActivity.EXTRA_ALLOW_SELECT_FROM_CONTENT_PROVIDERS, false))
            res.add(new DrawerSelectContentProviderMenuItem(getDrawerController()));

        if (_allowDocumentTree)
            res.add(new DrawerManageLocalStorages(getDrawerController()));

        return res;
    }

    protected boolean _allowDeviceLocations, _allowDocumentTree;

    protected void addLocationMenuItem(List<DrawerMenuItemBase> list, Location loc)
    {
        if (loc instanceof InternalSDLocation && _allowDeviceLocations)
            list.add(new DrawerInternalSDMenuItem(loc, getDrawerController()));
        else if (loc instanceof ExternalStorageLocation && _allowDeviceLocations)
            list.add(new DrawerExternalSDMenuItem(loc, getDrawerController(), _allowDocumentTree));
        else if (loc instanceof DocumentTreeLocation && _allowDocumentTree)
            list.add(new DrawerDocumentTreeMenuItem(loc, getDrawerController()));
    }
}
