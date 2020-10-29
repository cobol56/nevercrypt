package com.igeltech.nevercrypt.android.settings.container;

import android.os.Bundle;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.dialogs.PasswordDialog;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragment;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragmentBase;
import com.igeltech.nevercrypt.android.settings.PropertyEditor;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Openable;

public class SavePasswordPropertyEditorBase extends SwitchPropertyEditor implements PasswordDialog.PasswordReceiver
{
    public SavePasswordPropertyEditorBase(LocationSettingsFragmentBase settingsFragment)
    {
        super(settingsFragment, R.string.save_password, R.string.save_password_desc);
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
        return !loc.requirePassword();
    }

    @Override
    protected void saveValue(boolean value)
    {

    }

    @Override
    public void onPasswordEntered(PasswordDialog dlg)
    {
        CryptoLocation.ExternalSettings settings = getHost().getLocation().getExternalSettings();
        SecureBuffer sb = new SecureBuffer(dlg.getPassword());
        byte[] data = sb.getDataArray();
        settings.setPassword(data);
        SecureBuffer.eraseData(data);
        sb.close();
        getHost().saveExternalSettings();
    }

    @Override
    public void onPasswordNotEntered(PasswordDialog dlg)
    {
        _switchButton.setChecked(false);
    }

    @Override
    protected boolean onChecked(boolean isChecked)
    {
        Openable loc = getHost().getLocation();
        if (isChecked)
        {
            Bundle args = new Bundle();
            args.putBoolean(PasswordDialog.ARG_HAS_PASSWORD, loc.hasPassword());
            args.putString(PasswordDialog.ARG_RECEIVER_FRAGMENT_TAG, getHost().getTag());
            args.putInt(PropertyEditor.ARG_PROPERTY_ID, getId());
            PasswordDialog pd = new PasswordDialog();
            pd.setArguments(args);
            pd.show(getHost().getFragmentManager(), PasswordDialog.TAG);
            return true;
        }
        else
        {
            getHost().getLocation().getExternalSettings().setPassword(null);
            getHost().saveExternalSettings();
            return true;
        }
    }
}
