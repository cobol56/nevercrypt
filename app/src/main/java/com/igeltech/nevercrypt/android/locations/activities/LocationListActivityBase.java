package com.igeltech.nevercrypt.android.locations.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.helpers.CompatHelper;
import com.igeltech.nevercrypt.android.locations.ContainerBasedLocation;
import com.igeltech.nevercrypt.android.locations.DocumentTreeLocation;
import com.igeltech.nevercrypt.android.locations.fragments.ContainerListFragment;
import com.igeltech.nevercrypt.android.locations.fragments.DocumentTreeLocationsListFragment;
import com.igeltech.nevercrypt.android.locations.fragments.LocationListBaseFragment;
import com.igeltech.nevercrypt.android.settings.UserSettings;

public abstract class LocationListActivityBase extends AppCompatActivity
{
    public static final String EXTRA_LOCATION_TYPE = "com.igeltech.nevercrypt.android.LOCATION_TYPE";

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(UserSettings.getSettings(this).isFlagSecureEnabled())
            CompatHelper.setWindowFlagSecure(this);
        if(savedInstanceState == null)
            getSupportFragmentManager().
                beginTransaction().
                add(android.R.id.content, getCreateLocationFragment(), LocationListBaseFragment.TAG).
                commit();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    protected Fragment getCreateLocationFragment()
    {
        switch (getIntent().getStringExtra(EXTRA_LOCATION_TYPE))
        {
            case ContainerBasedLocation.URI_SCHEME:
                return new ContainerListFragment();
            case DocumentTreeLocation.URI_SCHEME:
                return new DocumentTreeLocationsListFragment();
            default:
                throw new RuntimeException("Unknown location type");
        }
    }

}
