package com.igeltech.nevercrypt.android.locations.fragments;

import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.locations.EncFsLocationBase;
import com.igeltech.nevercrypt.android.locations.opener.fragments.EDSLocationOpenerFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.LocationOpenerBaseFragment;
import com.igeltech.nevercrypt.android.tasks.ChangeEncFsPasswordTask;

public class EncFsSettingsFragment extends EDSLocationSettingsFragment
{
    @Override
    public EncFsLocationBase getLocation()
    {
        return (EncFsLocationBase) super.getLocation();
    }

    @Override
    protected TaskFragment createChangePasswordTaskInstance()
    {
        return new ChangeEncFsPasswordTask();
    }

    @Override
    protected LocationOpenerBaseFragment getLocationOpener()
    {
        return new EDSLocationOpenerFragment();
    }
}
