package com.igeltech.nevercrypt.android.settings.activities;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.settings.fragments.ProgramSettingsFragment;

public class ProgramSettingsActivity extends SettingsBaseActivity
{
    @Override
    protected Fragment getSettingsFragment()
    {
        return new ProgramSettingsFragment();
    }
}
