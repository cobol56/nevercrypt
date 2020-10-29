package com.igeltech.nevercrypt.android.tasks;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.helpers.ProgressDialogTaskFragmentCallbacks;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragment;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class WriteSettingsTask extends TaskFragment
{
    public static final String ARG_FIN_ACTIVITY = "com.igeltech.nevercrypt.android.FIN_ACTIVITY";
    private Context _context;

    public static WriteSettingsTask newInstance(CryptoLocation cont, boolean finActivity)
    {
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIN_ACTIVITY, finActivity);
        LocationsManager.storePathsInBundle(args, cont, null);
        WriteSettingsTask f = new WriteSettingsTask();
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
        cont.applyInternalSettings();
        cont.writeInternalSettings();
    }

    @Override
    protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
    {
        LocationSettingsFragment f = (LocationSettingsFragment) getFragmentManager().findFragmentByTag(SettingsBaseActivity.SETTINGS_FRAGMENT_TAG);
        if (f == null)
            return null;
        return new ProgressDialogTaskFragmentCallbacks(activity, R.string.saving_changes)
        {
            @Override
            public void onCompleted(Bundle args, TaskFragment.Result result)
            {
                super.onCompleted(args, result);
                try
                {
                    result.getResult();
                    if (args.getBoolean(WriteSettingsTask.ARG_FIN_ACTIVITY, false))
                        getActivity().finish();
                }
                catch (Throwable e)
                {
                    Logger.showAndLog(_context, result.getError());
                }
            }
        };
    }
}
