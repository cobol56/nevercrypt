package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.settings.ChoiceDialogPropertyEditor;
import com.igeltech.nevercrypt.fs.encfs.AlgInfo;
import com.igeltech.nevercrypt.util.RefVal;

import java.util.ArrayList;

public abstract class CodecInfoPropertyEditor extends ChoiceDialogPropertyEditor
{
    public CodecInfoPropertyEditor(CreateLocationFragment hostFragment, int titleId, int descrId)
    {
        super(hostFragment, titleId, descrId, hostFragment.getTag());
    }

    protected abstract Iterable<? extends AlgInfo> getCodecs();
    protected abstract String getParamName();

    protected int findCodec(String name, RefVal<AlgInfo> codec)
    {
        int i = 0;
        for(AlgInfo ci: getCodecs())
        {
            if(name.equals(ci.getName()))
            {
                if(codec!=null)
                    codec.value = ci;
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    protected int loadValue()
    {
        String encAlgName = getHostFragment().getState().getString(getParamName());
        return encAlgName != null ? findCodec(encAlgName, null) : 0;
    }

    @Override
    protected void saveValue(int value)
    {
        int i = 0;
        for(AlgInfo ci: getCodecs())
        {
            if(i == value)
            {
                getHostFragment().getState().putString(getParamName(), ci.getName());
                return;
            }
            i++;
        }
        getHostFragment().getState().remove(getParamName());
    }

    @Override
    protected ArrayList<String> getEntries()
    {
        ArrayList<String> res = new ArrayList<>();
        for(AlgInfo ci: getCodecs())
            res.add(ci.getDescr());
        return res;
    }

    protected CreateLocationFragment getHostFragment()
    {
        return (CreateLocationFragment) getHost();
    }

}
