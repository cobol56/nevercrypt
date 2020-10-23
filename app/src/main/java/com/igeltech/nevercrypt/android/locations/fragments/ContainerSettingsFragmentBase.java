package com.igeltech.nevercrypt.android.locations.fragments;

import android.os.Bundle;

import com.igeltech.nevercrypt.android.dialogs.PasswordDialog;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.ContainerOpenerFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.LocationOpenerBaseFragment;
import com.igeltech.nevercrypt.android.settings.container.ContainerFormatHintPropertyEditor;
import com.igeltech.nevercrypt.android.settings.container.EncEngineHintPropertyEditor;
import com.igeltech.nevercrypt.android.settings.container.HashAlgHintPropertyEditor;
import com.igeltech.nevercrypt.android.tasks.ChangeContainerPasswordTask;
import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.Container;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.locations.ContainerLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.locations.Openable;

import java.util.Collection;
import java.util.List;

public class ContainerSettingsFragmentBase extends LocationSettingsFragment
{
    @Override
    public ContainerLocation getLocation()
    {
        return (ContainerLocation) super.getLocation();
    }

    public ContainerFormatInfo getCurrentContainerFormat()
    {
        List<ContainerFormatInfo> supportedFormats = getLocation().getSupportedFormats();
        return supportedFormats.size() == 1 ?
                supportedFormats.get(0) :
                Container.findFormatByName(
                        supportedFormats,
                        getLocation().getExternalSettings().getContainerFormatName()
                );
    }

    @Override
    protected TaskFragment createChangePasswordTaskInstance()
    {
        return new ChangeContainerPasswordTask();
    }

    @Override
    protected LocationOpenerBaseFragment getLocationOpener()
    {
        return new ContainerOpenerFragment();
    }

    @Override
    protected void createStdProperties(Collection<Integer> ids)
    {
        super.createStdProperties(ids);
        createHintProperties(ids);
    }

    @Override
    protected Bundle getChangePasswordTaskArgs(PasswordDialog dlg)
    {
        final Bundle args = new Bundle();
        args.putAll(dlg.getOptions());
        args.putParcelable(Openable.PARAM_PASSWORD, new SecureBuffer(dlg.getPassword()));
        LocationsManager.storePathsInBundle(args, getLocation(), null);
        return args;
    }

    protected void createHintProperties(Collection<Integer> ids)
    {
        ids.add(_propertiesView.addProperty(new ContainerFormatHintPropertyEditor(this)));
        ids.add(_propertiesView.addProperty(new EncEngineHintPropertyEditor(this)));
        ids.add(_propertiesView.addProperty(new HashAlgHintPropertyEditor(this)));
    }
}
