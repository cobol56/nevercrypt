package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragmentBase;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEncFsTaskFragment;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;

public class EnableEmptyBlocksPropertyEditor extends SwitchPropertyEditor
{
    public EnableEmptyBlocksPropertyEditor(CreateLocationFragment hostFragment)
    {
        super(hostFragment, R.string.allow_empty_blocks, R.string.allow_empty_blocks_descr);
    }

    @Override
    protected boolean loadValue()
    {
        return getHostFragment().getState().getBoolean(CreateEncFsTaskFragment.ARG_ALLOW_EMPTY_BLOCKS, true);
    }

    @Override
    protected void saveValue(boolean value)
    {
        getHostFragment().getState().putBoolean(CreateEncFsTaskFragment.ARG_ALLOW_EMPTY_BLOCKS, value);
    }

    protected CreateContainerFragmentBase getHostFragment()
    {
        return (CreateContainerFragmentBase) getHost();
    }
}
