package com.igeltech.nevercrypt.android.settings.container;

import android.os.Bundle;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.dialogs.PasswordDialog;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragment;
import com.igeltech.nevercrypt.android.locations.fragments.LocationSettingsFragmentBase;
import com.igeltech.nevercrypt.android.settings.ButtonPropertyEditor;
import com.igeltech.nevercrypt.android.settings.PropertyEditor;
import com.igeltech.nevercrypt.android.tasks.ChangeContainerPasswordTask;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class ChangePasswordPropertyEditor extends ButtonPropertyEditor implements PasswordDialog.PasswordReceiver
{
    public ChangePasswordPropertyEditor(LocationSettingsFragmentBase settingsFragment)
    {
        super(settingsFragment, R.string.change_container_password, 0, R.string.enter_new_password);
    }

	@Override
	public LocationSettingsFragment getHost()
	{
		return (LocationSettingsFragment) super.getHost();
	}

    @Override
    protected void onButtonClick()
    {
        Bundle args = new Bundle();
        args.putBoolean(PasswordDialog.ARG_HAS_PASSWORD, true);
        args.putBoolean(PasswordDialog.ARG_VERIFY_PASSWORD, true);
        args.putInt(PropertyEditor.ARG_PROPERTY_ID, getId());
        Location loc = getHost().getLocation();
        LocationsManager.storePathsInBundle(args, loc, null);
        args.putString(PasswordDialog.ARG_RECEIVER_FRAGMENT_TAG, getHost().getTag());
        PasswordDialog pd = new PasswordDialog();
        pd.setArguments(args);
        pd.show(getHost().getFragmentManager(), PasswordDialog.TAG);
    }

    @Override
    public void onPasswordEntered(final PasswordDialog dlg)
    {
        getHost().getResHandler().addResult(() -> getHost().getFragmentManager().
                beginTransaction().
                add(
                        getHost().getChangePasswordTask(dlg),
                        ChangeContainerPasswordTask.TAG).
                commit());

    }

    @Override
    public void onPasswordNotEntered(PasswordDialog dlg){}

}
