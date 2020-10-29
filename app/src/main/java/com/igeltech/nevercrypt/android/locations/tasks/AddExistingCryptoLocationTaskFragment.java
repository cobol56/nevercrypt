package com.igeltech.nevercrypt.android.locations.tasks;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.container.ContainerFormatterBase;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

public abstract class AddExistingCryptoLocationTaskFragment extends TaskFragment
{
    protected static final String ARG_STORE_LINK = "com.igeltech.nevercrypt.android.STORE_LINK";
    protected Context _context;

    @Override
    public void initTask(FragmentActivity activity)
    {
        _context = activity.getApplicationContext();
    }

    @Override
    protected void doWork(TaskState state) throws Exception
    {
        LocationsManager lm = LocationsManager.getLocationsManager(_context);
        Location location = lm.getFromBundle(getArguments(), null);
        state.setResult(findOrCreateLocation(lm, location, getArguments().getBoolean(ARG_STORE_LINK)));
    }

    @Override
    protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
    {
        CreateLocationFragment f = (CreateLocationFragment) getFragmentManager().findFragmentByTag(SettingsBaseActivity.SETTINGS_FRAGMENT_TAG);
        return f == null ? null : f.getAddExistingCryptoLocationTaskCallbacks();
    }

    protected CryptoLocation findOrCreateLocation(LocationsManager lm, Location locationLocation, boolean storeLink) throws Exception
    {
        CryptoLocation loc = createCryptoLocation(locationLocation);
        CryptoLocation exCont = (CryptoLocation) lm.findExistingLocation(loc);
        if (exCont != null)
        {
            if (lm.isStoredLocation(exCont.getId()) && exCont.getClass().equals(loc.getClass()))
            {
                exCont.getExternalSettings().setVisibleToUser(true);
                if (storeLink)
                    exCont.saveExternalSettings();
                return exCont;
            }
            else
                lm.removeLocation(exCont);
        }
        addCryptoLocation(lm, loc, storeLink);
        setLocationSettings(loc, storeLink);
        return loc;
    }

    protected void setLocationSettings(CryptoLocation loc, boolean storeLink)
    {
        loc.getExternalSettings().setTitle(ContainerFormatterBase.makeTitle(loc, LocationsManager.getLocationsManager(_context)));
        loc.getExternalSettings().setVisibleToUser(true);
        if (storeLink)
            loc.saveExternalSettings();
    }

    protected void addCryptoLocation(LocationsManager lm, CryptoLocation loc, boolean storeLink) throws Exception
    {
        lm.replaceLocation(loc, loc, storeLink);
    }

    protected abstract CryptoLocation createCryptoLocation(Location locationLocation) throws Exception;
}
