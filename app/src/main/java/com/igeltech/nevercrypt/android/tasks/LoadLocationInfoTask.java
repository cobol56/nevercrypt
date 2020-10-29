package com.igeltech.nevercrypt.android.tasks;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragment;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class LoadLocationInfoTask extends TaskFragment
{
    public static final String TAG = "com.igeltech.nevercrypt.android.tasks.LoadLocationInfoTask";

    public static LoadLocationInfoTask newInstance(CryptoLocation location)
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
        CryptoLocation cont = (CryptoLocation) LocationsManager.getLocationsManager(_context).getFromBundle(getArguments(), null);
        LocationSettingsFragment.LocationInfo info = initParams();
        fillInfo(cont, info);
        state.setResult(info);
    }

    @Override
    protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
    {
        final LocationSettingsFragment f = (LocationSettingsFragment) getFragmentManager().findFragmentByTag(SettingsBaseActivity.SETTINGS_FRAGMENT_TAG);
        return f != null ? f.getLoadLocationInfoTaskCallbacks() : null;
    }

    protected LocationSettingsFragment.LocationInfo initParams()
    {
        return new LocationSettingsFragment.LocationInfo();
    }

    protected void fillInfo(CryptoLocation location, LocationSettingsFragment.LocationInfo info) throws Exception
    {
        info.pathToLocation = location.getLocation().toString();
        if (location.isOpenOrMounted())
        {
            info.totalSpace = location.getCurrentPath().getDirectory().getTotalSpace();
            info.freeSpace = location.getCurrentPath().getDirectory().getFreeSpace();
        }
    }

    protected Context _context;
}
