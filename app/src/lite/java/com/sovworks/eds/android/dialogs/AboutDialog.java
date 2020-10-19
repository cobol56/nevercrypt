package com.sovworks.eds.android.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sovworks.eds.android.R;
import com.sovworks.eds.settings.GlobalConfig;

public class AboutDialog extends AboutDialogBase
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if(v == null)
            return null;
        v.findViewById(R.id.donation_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openWebPage(GlobalConfig.DONATIONS_URL);
            }
        });

        v.findViewById(R.id.check_source_code_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openWebPage(GlobalConfig.SOURCE_CODE_URL);
            }
        });

        return v;
    }
}
