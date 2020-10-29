package com.igeltech.nevercrypt.android.locations.opener.fragments;

import android.os.Bundle;

import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.providers.ContainersDocumentProviderBase;
import com.igeltech.nevercrypt.android.service.LocationsService;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.locations.Openable;

public class LocationOpenerFragment extends LocationOpenerFragmentCommon implements LocationOpenerBaseFragment.LocationOpenerResultReceiver
{
    public static class OpenLocationTaskFragment extends LocationOpenerFragmentCommon.OpenLocationTaskFragment
    {
        @Override
        protected void openLocation(Openable location, Bundle param) throws Exception
        {
            if (!location.isOpen())
            {
                super.openLocation(location, param);
                if (location instanceof CryptoLocation)
                {
                    LocationsService.registerInactiveContainerCheck(_context, (CryptoLocation) location);
                    ContainersDocumentProviderBase.notifyOpenedLocationsListChanged(_context);
                }
            }
        }
    }

    @Override
    public void onTargetLocationOpened(Bundle openerArgs, Location location)
    {
        openLocation();
    }

    @Override
    public void onTargetLocationNotOpened(Bundle openerArgs)
    {
        finishOpener(false, null);
    }

    @Override
    protected void openLocation()
    {
        CryptoLocation cbl = getTargetLocation();
        Location baseLocation = cbl.getLocation();
        if (baseLocation instanceof Openable && !((Openable) baseLocation).isOpen())
        {
            LocationOpenerBaseFragment f = getDefaultOpenerForLocation(baseLocation);
            Bundle b = new Bundle();
            LocationsManager.storePathsInBundle(b, baseLocation, null);
            b.putString(PARAM_RECEIVER_FRAGMENT_TAG, getTag());
            f.setArguments(b);
            getFragmentManager().beginTransaction().add(f, getOpenerTag(baseLocation)).commit();
        }
        else
            super.openLocation();
    }

    @Override
    protected CryptoLocation getTargetLocation()
    {
        return (CryptoLocation) super.getTargetLocation();
    }

    @Override
    protected TaskFragment getOpenLocationTask()
    {
        return new OpenLocationTaskFragment();
    }
}
