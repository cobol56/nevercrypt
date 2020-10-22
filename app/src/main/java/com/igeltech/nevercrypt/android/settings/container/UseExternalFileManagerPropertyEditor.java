package com.igeltech.nevercrypt.android.settings.container;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.PropertiesHostWithLocation;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;
import com.igeltech.nevercrypt.locations.Location;

public class UseExternalFileManagerPropertyEditor extends SwitchPropertyEditor
{
    public UseExternalFileManagerPropertyEditor(PropertiesHostWithLocation host)
    {
        super(host, R.string.use_external_file_manager, 0);
    }

    @Override
    public PropertiesHostWithLocation getHost()
    {
        return (PropertiesHostWithLocation) super.getHost();
    }

    @Override
    protected void saveValue(boolean value)
    {
        getLocation().getExternalSettings().setUseExtFileManager(value);
        if(getHost().getPropertiesView().isInstantSave())
            getLocation().saveExternalSettings();
    }

    @Override
    protected boolean loadValue()
    {
        return getLocation().getExternalSettings().useExtFileManager();
    }

    private Location getLocation()
    {
        return getHost().getTargetLocation();
    }
}
