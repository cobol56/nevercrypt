package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragment;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragmentBase;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEncFsTaskFragment;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;

public class UniqueIVPropertyEditor extends SwitchPropertyEditor
{
    public UniqueIVPropertyEditor(CreateContainerFragmentBase hostFragment)
    {
        super(hostFragment, R.string.enable_per_file_iv, R.string.enable_per_file_iv_descr);
    }

    @Override
    protected boolean loadValue()
    {
        getHostFragment().changeUniqueIVDependentOptions();
        return getHostFragment().getState().getBoolean(CreateEncFsTaskFragment.ARG_UNIQUE_IV, true);
    }

    @Override
    protected void saveValue(boolean value)
    {
        getHostFragment().getState().putBoolean(CreateEncFsTaskFragment.ARG_UNIQUE_IV, value);
        getHostFragment().changeUniqueIVDependentOptions();
    }

    protected CreateContainerFragment getHostFragment()
    {
        return (CreateContainerFragment) getHost();
    }
}
