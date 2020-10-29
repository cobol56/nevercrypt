package com.igeltech.nevercrypt.android.locations.activities;

import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.locations.ContainerBasedLocation;
import com.igeltech.nevercrypt.android.locations.EncFsLocationBase;
import com.igeltech.nevercrypt.android.locations.LUKSLocation;
import com.igeltech.nevercrypt.android.locations.TrueCryptLocation;
import com.igeltech.nevercrypt.android.locations.VeraCryptLocation;
import com.igeltech.nevercrypt.android.locations.fragments.ContainerSettingsFragment;
import com.igeltech.nevercrypt.android.locations.fragments.EncFsSettingsFragment;

public class LocationSettingsActivity extends SettingsBaseActivity
{
    @Override
    protected Fragment getSettingsFragment()
    {
        return getCreateLocationFragment();
    }

    private Fragment getCreateLocationFragment()
    {
        Uri uri = getIntent().getData();
        if (uri == null || uri.getScheme() == null)
            throw new RuntimeException("Location uri is not set");
        switch (uri.getScheme())
        {
            case EncFsLocationBase.URI_SCHEME:
                return new EncFsSettingsFragment();
            case VeraCryptLocation.URI_SCHEME:
            case TrueCryptLocation.URI_SCHEME:
            case LUKSLocation.URI_SCHEME:
            case ContainerBasedLocation.URI_SCHEME:
                return new ContainerSettingsFragment();
            default:
                throw new RuntimeException("Unknown location type");
        }
    }
}
