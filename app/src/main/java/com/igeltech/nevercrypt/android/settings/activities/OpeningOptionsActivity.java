package com.igeltech.nevercrypt.android.settings.activities;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.settings.PropertiesHostWithStateBundle;
import com.igeltech.nevercrypt.android.settings.fragments.OpeningOptionsFragment;

public class OpeningOptionsActivity extends SettingsBaseActivity
{
    @Override
    public void onBackPressed()
    {
        PropertiesHostWithStateBundle frag = (PropertiesHostWithStateBundle) getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
        if(frag!=null)
        {
            try
            {
                frag.getPropertiesView().saveProperties();
                Intent res = new Intent();
                res.putExtras(frag.getState());
                setResult(RESULT_OK, res);
                super.onBackPressed();
            }
            catch (Exception e)
            {
                Logger.showAndLog(this, e);
            }
        }
        else
            super.onBackPressed();
    }

    @Override
    protected Fragment getSettingsFragment()
    {
        return getOpeningOptionsFragment();
    }

    private Fragment getOpeningOptionsFragment()
    {
        return new OpeningOptionsFragment();
    }

}
