package com.igeltech.nevercrypt.android.locations.dialogs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.dialogs.ConfirmationDialog;
import com.igeltech.nevercrypt.android.locations.closer.fragments.LocationCloserBaseFragment;

public class ForceCloseDialog extends ConfirmationDialog
{
    public static final String ARG_LOCATION_TITLE = "com.igeltech.nevercrypt.android.LOCATION_TITLE";
    public static final String ARG_CLOSER_ARGS = "com.igeltech.nevercrypt.android.CLOSER_ARGS";
    public static final String ARG_CLOSER_CLASS_NAME = "com.igeltech.nevercrypt.android.CLOSER_CLASS_NAME";

    public static final String TAG = "ForceCloseDialog";

    public static void showDialog(FragmentManager fm, String closerTag, String locTitle, String closerClassName, Bundle closerArgs)
    {
        AppCompatDialogFragment f = new ForceCloseDialog();
        Bundle b = new Bundle();
        b.putString(ARG_LOCATION_TITLE, locTitle);
        b.putString(ARG_CLOSER_CLASS_NAME, closerClassName);
        if(closerArgs!=null)
            b.putBundle(ARG_CLOSER_ARGS, closerArgs);
        b.putString(LocationCloserBaseFragment.PARAM_RECEIVER_FRAGMENT_TAG, closerTag);
        f.setArguments(b);
        f.show(fm, TAG);
    }

	@Override
	protected void onYes()
	{
        Bundle closerArgs = getArguments().getBundle(ARG_CLOSER_ARGS);
        if(closerArgs == null)
            closerArgs = new Bundle();
        closerArgs.putBoolean(LocationCloserBaseFragment.ARG_FORCE_CLOSE, true);
        LocationCloserBaseFragment closer = (LocationCloserBaseFragment) Fragment.instantiate(
                getActivity(),
                getArguments().getString(ARG_CLOSER_CLASS_NAME),
                closerArgs
        );
        getFragmentManager().beginTransaction().add(closer, getArguments().getString(LocationCloserBaseFragment.PARAM_RECEIVER_FRAGMENT_TAG)).commit();
	}

    @Override
    protected String getTitle()
    {
        return getString(R.string.force_close_request, getArguments().getString(ARG_LOCATION_TITLE));
    }
}
