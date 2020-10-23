package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragmentBase;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEncFsTaskFragment;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;

public class ExternalFileIVPropertyEditor extends SwitchPropertyEditor
{
    public ExternalFileIVPropertyEditor(CreateLocationFragment hostFragment)
    {
        super(hostFragment, R.string.enable_filename_to_file_iv_chain, R.string.enable_filename_to_file_iv_chain_descr);
    }

    @Override
    protected boolean loadValue()
    {
        return getHostFragment().getState().getBoolean(CreateEncFsTaskFragment.ARG_EXTERNAL_IV, false);
    }

    @Override
    protected void saveValue(boolean value)
    {
        getHostFragment().getState().putBoolean(CreateEncFsTaskFragment.ARG_EXTERNAL_IV, value);
    }

    protected CreateContainerFragmentBase getHostFragment()
    {
        return (CreateContainerFragmentBase) getHost();
    }
}
