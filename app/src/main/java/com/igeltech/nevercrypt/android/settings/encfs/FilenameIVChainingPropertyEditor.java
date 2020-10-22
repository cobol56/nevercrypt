package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragment;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragmentBase;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEncFsTaskFragment;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;

public class FilenameIVChainingPropertyEditor extends SwitchPropertyEditor
{
    public FilenameIVChainingPropertyEditor(CreateContainerFragmentBase hostFragment)
    {
        super(hostFragment, R.string.enable_filename_iv_chain, R.string.enable_filename_iv_chain_descr);
    }

    @Override
    protected boolean loadValue()
    {
        getHostFragment().changeUniqueIVDependentOptions();
        return getHostFragment().getState().getBoolean(CreateEncFsTaskFragment.ARG_CHAINED_NAME_IV, true);
    }

    @Override
    protected void saveValue(boolean value)
    {
        getHostFragment().getState().putBoolean(CreateEncFsTaskFragment.ARG_CHAINED_NAME_IV, value);
        getHostFragment().changeUniqueIVDependentOptions();
    }

    protected CreateContainerFragment getHostFragment()
    {
        return (CreateContainerFragment) getHost();
    }
}
