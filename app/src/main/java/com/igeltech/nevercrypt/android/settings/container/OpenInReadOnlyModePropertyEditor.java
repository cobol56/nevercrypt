package com.igeltech.nevercrypt.android.settings.container;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.PropertiesHostWithLocation;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;
import com.igeltech.nevercrypt.locations.EDSLocation;

public class OpenInReadOnlyModePropertyEditor extends SwitchPropertyEditor
{
    public OpenInReadOnlyModePropertyEditor(PropertiesHostWithLocation host)
    {
        super(host, R.string.open_read_only, 0);
    }

    @Override
    public PropertiesHostWithLocation getHost()
    {
        return (PropertiesHostWithLocation) super.getHost();
    }

    @Override
    protected void saveValue(boolean value)
    {
        getLocation().getExternalSettings().setOpenReadOnly(value);
        if(getHost().getPropertiesView().isInstantSave())
            getLocation().saveExternalSettings();
    }

    @Override
    protected boolean loadValue()
    {
        return getLocation().getExternalSettings().shouldOpenReadOnly();
    }

    private EDSLocation getLocation()
    {
        return (EDSLocation) getHost().getTargetLocation();
    }
}
