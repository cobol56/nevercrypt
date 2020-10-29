package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEncFsTaskFragment;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;

public class MACBytesPerBlockPropertyEditor extends SwitchPropertyEditor
{
    public MACBytesPerBlockPropertyEditor(CreateLocationFragment hostFragment)
    {
        super(hostFragment, R.string.mac_bytes_per_block, R.string.mac_bytes_per_block_descr);
    }

    @Override
    protected boolean loadValue()
    {
        return getHostFragment().getState().getInt(CreateEncFsTaskFragment.ARG_MAC_BYTES, 0) > 0;
    }

    @Override
    protected void saveValue(boolean value)
    {
        if (value)
            getHostFragment().getState().putInt(CreateEncFsTaskFragment.ARG_MAC_BYTES, 8);
        else
            getHostFragment().getState().remove(CreateEncFsTaskFragment.ARG_MAC_BYTES);
    }

    protected CreateLocationFragment getHostFragment()
    {
        return (CreateLocationFragment) getHost();
    }
}
