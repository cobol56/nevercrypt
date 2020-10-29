package com.igeltech.nevercrypt.android.settings.container;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.IntPropertyEditor;
import com.igeltech.nevercrypt.android.settings.PropertiesHostWithStateBundle;
import com.igeltech.nevercrypt.locations.Openable;

public class PIMPropertyEditor extends IntPropertyEditor
{
    public PIMPropertyEditor(PropertiesHostWithStateBundle hostFragment)
    {
        super(hostFragment, R.string.kdf_iterations_multiplier, R.string.number_of_kdf_iterations_veracrypt_descr, ((Fragment) hostFragment).getTag());
    }

    @Override
    public PropertiesHostWithStateBundle getHost()
    {
        return (PropertiesHostWithStateBundle) super.getHost();
    }

    @Override
    protected int loadValue()
    {
        int val = getHost().getState().getInt(Openable.PARAM_KDF_ITERATIONS, 0);
        return val < 0 ? 0 : val;
    }

    @Override
    protected void saveValue(int value)
    {
        if (value < 0)
            value = 0;
        else if (value > 100000)
            value = 100000;
        getHost().getState().putInt(Openable.PARAM_KDF_ITERATIONS, value);
    }
}
