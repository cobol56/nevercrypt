package com.igeltech.nevercrypt.android.locations.activities;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragment;

public class CreateLocationActivity extends SettingsBaseActivity
{
    public static final String EXTRA_LOCATION_TYPE = "com.igeltech.nevercrypt.android.LOCATION_TYPE";

    @Override
    protected Fragment getSettingsFragment()
    {
        return getCreateLocationFragment();
    }

    private Fragment getCreateLocationFragment()
    {
        return new CreateContainerFragment();

    }
}
