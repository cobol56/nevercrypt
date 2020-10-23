package com.igeltech.nevercrypt.android.settings.encfs;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.locations.tasks.CreateEncFsTaskFragment;
import com.igeltech.nevercrypt.fs.encfs.AlgInfo;
import com.igeltech.nevercrypt.fs.encfs.FS;

public class NameCodecPropertyEditor extends CodecInfoPropertyEditor
{
    public NameCodecPropertyEditor(CreateLocationFragment hostFragment)
    {
        super(hostFragment, R.string.filename_encryption_algorithm, 0);
    }

    @Override
    protected Iterable<? extends AlgInfo> getCodecs()
    {
        return FS.getSupportedNameCodecs();
    }

    @Override
    protected String getParamName()
    {
        return CreateEncFsTaskFragment.ARG_NAME_CIPHER_NAME;
    }
}
