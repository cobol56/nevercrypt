package com.igeltech.nevercrypt.android.locations.activities;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.locations.ContainerBasedLocation;
import com.igeltech.nevercrypt.android.locations.TrueCryptLocation;
import com.igeltech.nevercrypt.android.locations.VeraCryptLocation;
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
        switch (getIntent().getStringExtra(EXTRA_LOCATION_TYPE))
        {
            case VeraCryptLocation.URI_SCHEME:
            case TrueCryptLocation.URI_SCHEME:
            case ContainerBasedLocation.URI_SCHEME:
                return new CreateContainerFragment();
            default:
                throw new RuntimeException("Unknown location type");
        }
    }
}
