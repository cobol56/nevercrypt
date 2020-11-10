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
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.io.IOException;

public abstract class ChangeLocationPasswordBaseTask extends TaskFragment
{
    protected CryptoLocation _location;
    protected Context _context;

    @Override
    public void initTask(FragmentActivity activity)
    {
        _context = activity.getApplicationContext();
        _location = (CryptoLocation) LocationsManager.getLocationsManager(_context).getFromBundle(getArguments(), null);
    }

    @Override
    protected void doWork(TaskState state) throws Exception
    {
        changeLocationPassword();
    }

    @Override
    protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
    {
        final LocationSettingsFragment f = (LocationSettingsFragment) getParentFragmentManager().findFragmentByTag(SettingsBaseActivity.SETTINGS_FRAGMENT_TAG);
        if (f == null)
            return null;
        return new ProgressDialogTaskFragmentCallbacks(activity, R.string.changing_password)
        {
            @Override
            public void onCompleted(Bundle args, Result result)
            {
                super.onCompleted(args, result);
                try
                {
                    result.getResult();
                    f.getPropertiesView().loadProperties();
                }
                catch (Throwable e)
                {
                    Logger.showAndLog(_host, result.getError());
                }
            }
        };
    }

    protected abstract void changeLocationPassword() throws IOException, ApplicationException;
}
