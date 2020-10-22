package com.igeltech.nevercrypt.android.settings.encfs;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.IntPropertyEditor;
import com.igeltech.nevercrypt.android.settings.PropertiesHostWithStateBundle;
import com.igeltech.nevercrypt.locations.Openable;

public class NumKDFIterationsPropertyEditor extends IntPropertyEditor
{
    public NumKDFIterationsPropertyEditor(PropertiesHostWithStateBundle hostFragment)
    {
        super(
                hostFragment,
                R.string.number_of_kdf_iterations,
                R.string.number_of_kdf_iterations_descr,
                ((Fragment)hostFragment).getTag()
        );
    }

    @Override
    public PropertiesHostWithStateBundle getHost()
    {
        return (PropertiesHostWithStateBundle) super.getHost();
    }

    @Override
    protected int loadValue()
    {
        return getHost().getState().getInt(Openable.PARAM_KDF_ITERATIONS, 100000);
    }


    @Override
    protected void saveValue(int value)
    {
        if(value < 1000)
            value = 1000;
        getHost().getState().putInt(Openable.PARAM_KDF_ITERATIONS, value);
    }
}
