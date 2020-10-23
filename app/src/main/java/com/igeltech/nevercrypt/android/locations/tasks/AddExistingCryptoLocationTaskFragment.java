package com.igeltech.nevercrypt.android.locations.tasks;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.locations.fragments.CreateEDSLocationFragment;
import com.igeltech.nevercrypt.container.ContainerFormatterBase;
import com.igeltech.nevercrypt.locations.EDSLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

public abstract class AddExistingEDSLocationTaskFragment extends TaskFragment
{

	@Override
	public void initTask(FragmentActivity activity)
	{
		_context = activity.getApplicationContext();
	}

    protected static final String ARG_STORE_LINK = "com.igeltech.nevercrypt.android.STORE_LINK";
	protected Context _context;

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
        CreateEDSLocationFragment f = (CreateEDSLocationFragment) getFragmentManager().findFragmentByTag(SettingsBaseActivity.SETTINGS_FRAGMENT_TAG);
        return f == null ? null : f.getAddExistingEDSLocationTaskCallbacks();
    }

    protected EDSLocation findOrCreateLocation(LocationsManager lm, Location locationLocation, boolean storeLink) throws Exception
    {
        EDSLocation loc = createEDSLocation(locationLocation);
        EDSLocation exCont = (EDSLocation) lm.findExistingLocation(loc);
        if(exCont != null)
        {
            if (lm.isStoredLocation(exCont.getId()) && exCont.getClass().equals(loc.getClass()))
            {
                exCont.getExternalSettings().setVisibleToUser(true);
                if(storeLink)
                    exCont.saveExternalSettings();
                return exCont;
            }
            else
                lm.removeLocation(exCont);
        }
        addEDSLocation(lm, loc, storeLink);
        setLocationSettings(loc, storeLink);
        return loc;
    }

    protected void setLocationSettings(EDSLocation loc, boolean storeLink)
    {
        loc.getExternalSettings().setTitle(ContainerFormatterBase.makeTitle(loc, LocationsManager.getLocationsManager(_context)));
        loc.getExternalSettings().setVisibleToUser(true);
        if (storeLink)
            loc.saveExternalSettings();
    }

	protected void addEDSLocation(LocationsManager lm, EDSLocation loc, boolean storeLink) throws Exception
    {
        lm.replaceLocation(loc, loc, storeLink);
	}

	protected abstract EDSLocation createEDSLocation(Location locationLocation) throws Exception;
}
