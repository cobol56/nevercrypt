package com.igeltech.nevercrypt.android.locations.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.locations.closer.fragments.LocationCloserBaseFragment;
import com.igeltech.nevercrypt.android.service.FileOpsService;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.util.Iterator;

public class ExitFragment extends Fragment implements LocationCloserBaseFragment.CloseLocationReceiver
{
    public static final String TAG = "com.igeltech.nevercrypt.android.ExitFragment";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        closeNextOrExit();
    }

    @Override
    public void onTargetLocationClosed(Location location, Bundle closeTaskArgs)
    {
        closeNextOrExit();
    }

    @Override
    public void onTargetLocationNotClosed(Location location, Bundle closeTaskArgs)
    {
    }

    private void closeNextOrExit()
    {
        Iterator<Location> it = LocationsManager.getLocationsManager(getActivity()).getLocationsClosingOrder().iterator();
        if (it.hasNext())
            launchCloser(it.next());
        else
            exit();
    }

    private void launchCloser(Location loc)
    {
        Bundle args = new Bundle();
        args.putString(LocationCloserBaseFragment.PARAM_RECEIVER_FRAGMENT_TAG, TAG);
        LocationsManager.storePathsInBundle(args, loc, null);
        Fragment closer = LocationCloserBaseFragment.getDefaultCloserForLocation(loc);
        closer.setArguments(args);
        getFragmentManager().beginTransaction().add(closer, LocationCloserBaseFragment.getCloserTag(loc)).commit();
    }

    private void exit()
    {
        FileOpsService.clearTempFolder(getActivity().getApplicationContext(), true);
        getActivity().finish();
    }
}
