package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEncFsTaskFragment;
import com.igeltech.nevercrypt.android.settings.ChoiceDialogPropertyEditor;

import java.util.ArrayList;
import java.util.List;

public class RandBytesPerBlockPropertyEditor extends ChoiceDialogPropertyEditor
{
    public RandBytesPerBlockPropertyEditor(CreateLocationFragment hostFragment)
    {
        super(hostFragment, R.string.add_rand_bytes, R.string.add_rand_bytes_descr, hostFragment.getTag());
    }

    @Override
    protected int loadValue()
    {
        return getHostFragment().getState().getInt(CreateEncFsTaskFragment.ARG_RAND_BYTES, 0);
    }

    @Override
    protected void saveValue(int value)
    {
        getHostFragment().getState().putInt(CreateEncFsTaskFragment.ARG_RAND_BYTES, value);
    }

    @Override
    protected List<String> getEntries()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(getHost().getContext().getString(R.string.disable));
        for(int i=1;i<=8;i++)
            res.add(String.valueOf(i));
        return res;
    }


    protected CreateLocationFragment getHostFragment()
    {
        return (CreateLocationFragment) getHost();
    }
}
