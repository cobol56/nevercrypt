package com.igeltech.nevercrypt.android.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.settings.GlobalConfig;

public class AboutDialog extends AboutDialogBase
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if(v == null)
            return null;
        v.findViewById(R.id.donation_button).setOnClickListener(view -> openWebPage(GlobalConfig.DONATIONS_URL));

        v.findViewById(R.id.check_source_code_button).setOnClickListener(view -> openWebPage(GlobalConfig.SOURCE_CODE_URL));

        return v;
    }
}
