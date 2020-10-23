package com.igeltech.nevercrypt.android.settings.container;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragment;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragmentBase;
import com.igeltech.nevercrypt.android.settings.PropertyEditor;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;
import com.igeltech.nevercrypt.android.settings.dialogs.TextEditDialog;
import com.igeltech.nevercrypt.locations.CryptoLocation;

public class SavePIMPropertyEditor extends SwitchPropertyEditor implements TextEditDialog.TextResultReceiver
{
    public SavePIMPropertyEditor(LocationSettingsFragmentBase settingsFragment)
    {
        super(settingsFragment, R.string.remember_kdf_iterations_multiplier, 0);
    }

	@Override
	public LocationSettingsFragment getHost()
	{
		return (LocationSettingsFragment) super.getHost();
	}

    @Override
    protected boolean loadValue()
    {
        CryptoLocation loc = getHost().getLocation();
        return !loc.requireCustomKDFIterations();
    }

    @Override
    public void setResult(String text) throws Exception
    {
        int val = text.isEmpty() ? 0 : Integer.valueOf(text);
        if(val < 0)
            val = 0;
        else if(val > 100000)
            val = 100000;
        getHost().getLocation().getExternalSettings().setCustomKDFIterations(val);
        getHost().saveExternalSettings();
    }

    @Override
    protected void saveValue(boolean value)
    {

    }

    @Override
    protected boolean onChecked(boolean isChecked)
    {
        if (isChecked)
        {
            startChangeValueDialog();
			return true;
        }
        else
        {
            getHost().getLocation().getExternalSettings().setCustomKDFIterations(-1);
            getHost().saveExternalSettings();
			return true;
        }
    }

    protected void startChangeValueDialog()
    {
        Bundle args = initDialogArgs();
        AppCompatDialogFragment df = new TextEditDialog();
        df.setArguments(args);
        df.show(getHost().getFragmentManager(), TextEditDialog.TAG);
    }

    protected int getDialogViewResId()
    {
        return R.layout.settings_edit_num;
    }

    protected Bundle initDialogArgs()
    {
        Bundle b = new Bundle();
        b.putInt(PropertyEditor.ARG_PROPERTY_ID, getId());
        b.putInt(TextEditDialog.ARG_MESSAGE_ID, _titleResId);
        b.putInt(TextEditDialog.ARG_EDIT_TEXT_RES_ID, getDialogViewResId());
        b.putString(PropertyEditor.ARG_HOST_FRAGMENT_TAG, getHost().getTag());
        return b;
    }
}
