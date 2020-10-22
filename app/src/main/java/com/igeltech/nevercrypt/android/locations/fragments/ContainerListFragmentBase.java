package com.igeltech.nevercrypt.android.locations.fragments;

import android.graphics.drawable.Drawable;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.ContainerBasedLocation;
import com.igeltech.nevercrypt.locations.EDSLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class ContainerListFragmentBase extends LocationListBaseFragment
{
    @Override
    protected void loadLocations()
    {
        _locationsList.clear();
		for(EDSLocation loc: LocationsManager.getLocationsManager(getActivity()).getLoadedEDSLocations(true))
                _locationsList.add(new ContainerInfo(loc));
    }

    @Override
    protected String getDefaultLocationType()
    {
        return ContainerBasedLocation.URI_SCHEME;
    }

    private class ContainerInfo extends LocationInfo
    {
        public ContainerInfo(EDSLocation ci)
        {
            location = ci;
        }

        @Override
        public boolean hasSettings() { return true; }

        @Override
        public Drawable getIcon()
        {
            return ((EDSLocation)location).isOpenOrMounted() ? getOpenedContainerIcon() : getClosedContainerIcon();
        }
    }

    private static Drawable _openedContainerIcon, _closedContainerIcon;

    private synchronized Drawable getOpenedContainerIcon()
    {
        if(_openedContainerIcon == null)
        {
            _openedContainerIcon = getResources().getDrawable(R.drawable.ic_lock_open );
        }
        return _openedContainerIcon;
    }

    private synchronized Drawable getClosedContainerIcon()
    {
        if(_closedContainerIcon == null)
        {
            _closedContainerIcon = getResources().getDrawable(R.drawable.ic_lock );
        }
        return _closedContainerIcon;
    }
}
