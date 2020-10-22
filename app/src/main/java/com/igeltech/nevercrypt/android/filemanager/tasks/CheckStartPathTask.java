package com.igeltech.nevercrypt.android.filemanager.tasks;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.locations.tasks.AddExistingContainerTaskFragment;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class CheckStartPathTask extends AddExistingContainerTaskFragment
{
    public static CheckStartPathTask newInstance(Uri startUri, boolean storeLink)
    {
        Bundle args = new Bundle();
        args.putBoolean(ARG_STORE_LINK, storeLink);
        args.putParcelable(LocationsManager.PARAM_LOCATION_URI, startUri);
        CheckStartPathTask f = new CheckStartPathTask();
        f.setArguments(args);
        return f;
    }

    @Override
    protected void doWork(TaskState state) throws Exception
    {
        LocationsManager lm = LocationsManager.getLocationsManager(_context);
        Location loc = lm.getFromBundle(getArguments(), null);
        if(loc.getCurrentPath().isFile())
            state.setResult(findOrCreateLocation(lm, loc, getArguments().getBoolean(ARG_STORE_LINK)));
        else
            state.setResult(null);
    }

    @Override
    protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
    {
        return ((FileManagerActivity)activity).getCheckStartPathCallbacks();
    }
}
