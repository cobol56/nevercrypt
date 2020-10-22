package com.igeltech.nevercrypt.android.tasks;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.locations.fragments.EDSLocationSettingsFragment;
import com.igeltech.nevercrypt.locations.EDSLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;


public class LoadLocationInfoTask extends TaskFragment
{
    public static final String TAG = "com.igeltech.nevercrypt.android.tasks.LoadLocationInfoTask";

	public static LoadLocationInfoTask newInstance(EDSLocation location)
    {
        Bundle args = new Bundle();
        LocationsManager.storePathsInBundle(args, location, null);
        LoadLocationInfoTask f = new LoadLocationInfoTask();
        f.setArguments(args);
		return f;
    }

    @Override
    protected void initTask(FragmentActivity activity)
    {
        super.initTask(activity);
        _context = activity.getApplicationContext();
    }

    @Override
    protected void doWork(TaskState state) throws Exception
    {
        EDSLocation cont = (EDSLocation) LocationsManager.getLocationsManager(_context).getFromBundle(getArguments(), null);
        EDSLocationSettingsFragment.LocationInfo info = initParams();
        fillInfo(cont, info);
		state.setResult(info);
	}

	@Override
    protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
    {
        final EDSLocationSettingsFragment f = (EDSLocationSettingsFragment) getFragmentManager().findFragmentByTag(SettingsBaseActivity.SETTINGS_FRAGMENT_TAG);
        return f != null ? f.getLoadLocationInfoTaskCallbacks() : null;
    }

    protected EDSLocationSettingsFragment.LocationInfo initParams()
    {
        return new EDSLocationSettingsFragment.LocationInfo();
    }

    protected void fillInfo(EDSLocation location, EDSLocationSettingsFragment.LocationInfo info) throws Exception
    {
        info.pathToLocation = location.getLocation().toString();
        if(location.isOpenOrMounted())
        {
            info.totalSpace = location.getCurrentPath().getDirectory().getTotalSpace();
            info.freeSpace = location.getCurrentPath().getDirectory().getFreeSpace();
        }
    }

    protected Context _context;

}
