package com.igeltech.nevercrypt.android.filemanager.tasks;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.filemanager.fragments.FileListViewFragment;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class OpenAsContainerTask extends CheckStartPathTask
{
    public static OpenAsContainerTask newInstance(Location locationLocation, boolean storeLink)
    {
        Bundle args = new Bundle();
        args.putBoolean(ARG_STORE_LINK, storeLink);
        LocationsManager.storePathsInBundle(args, locationLocation, null);
        OpenAsContainerTask f = new OpenAsContainerTask();
        f.setArguments(args);
        return f;
    }

    @Override
    protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
    {
        FileListViewFragment f = (FileListViewFragment) getFragmentManager().findFragmentByTag(FileListViewFragment.TAG);
        return f == null ? null : f.getOpenAsContainerTaskCallbacks();
    }
}
