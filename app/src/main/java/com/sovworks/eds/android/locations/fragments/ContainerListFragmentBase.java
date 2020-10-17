package com.sovworks.eds.android.locations.fragments;

import android.graphics.drawable.Drawable;

import com.sovworks.eds.android.R;
import com.sovworks.eds.android.locations.ContainerBasedLocation;
import com.sovworks.eds.locations.EDSLocation;
import com.sovworks.eds.locations.LocationsManager;

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
