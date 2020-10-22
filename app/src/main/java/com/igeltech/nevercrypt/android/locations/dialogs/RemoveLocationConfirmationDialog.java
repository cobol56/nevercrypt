package com.igeltech.nevercrypt.android.locations.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.dialogs.ConfirmationDialog;
import com.igeltech.nevercrypt.android.locations.fragments.LocationListBaseFragment;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class RemoveLocationConfirmationDialog extends ConfirmationDialog
{
    public static final String TAG = "RemoveLocationConfirmationDialog";

    public static void showDialog(FragmentManager fm, Location loc)
    {
        DialogFragment f = new RemoveLocationConfirmationDialog();
        Bundle b = new Bundle();
        LocationsManager.storePathsInBundle(b,loc, null);
        f.setArguments(b);
        f.show(fm, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _loc = LocationsManager.getLocationsManager(getActivity()).getFromBundle(getArguments(), null);
    }

    @Override
	protected void onYes()
	{
        if(_loc == null)
            return;
        LocationListBaseFragment f = (LocationListBaseFragment) getFragmentManager().findFragmentByTag(LocationListBaseFragment.TAG);
        if(f == null)
            return;
        f.removeLocation(_loc);
	}

    @Override
    protected String getTitle()
    {
        return getString(R.string.do_you_really_want_to_remove_from_the_list, _loc == null ? "" : _loc.getTitle());
    }

    private Location _loc;
}
