package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateEDSLocationFragment;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEDSLocationTaskFragment;
import com.igeltech.nevercrypt.android.settings.views.PropertiesView;
import com.igeltech.nevercrypt.fs.encfs.AlgInfo;
import com.igeltech.nevercrypt.fs.encfs.FS;

public class DataCodecPropertyEditor extends CodecInfoPropertyEditor
{
    public static final int ID = PropertiesView.newId();

    public DataCodecPropertyEditor(CreateEDSLocationFragment hostFragment)
    {
        super(hostFragment, R.string.encryption_algorithm, 0);
        setId(ID);
    }

    @Override
    protected Iterable<? extends AlgInfo> getCodecs()
    {
        return FS.getSupportedDataCodecs();
    }

    @Override
    protected String getParamName()
    {
        return CreateEDSLocationTaskFragment.ARG_CIPHER_NAME;
    }
}
