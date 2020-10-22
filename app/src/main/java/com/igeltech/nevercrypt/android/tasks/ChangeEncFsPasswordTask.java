package com.igeltech.nevercrypt.android.tasks;

import android.os.Bundle;

import com.igeltech.nevercrypt.android.helpers.Util;
import com.igeltech.nevercrypt.android.locations.EncFsLocationBase;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.io.IOException;

public class ChangeEncFsPasswordTask extends ChangeEDSLocationPasswordTask
{
    public static final String TAG = "com.igeltech.nevercrypt.android.tasks.ChangeContainerPasswordTask";
    //public static final String ARG_FIN_ACTIVITY = "fin_activity";

	public static ChangeEncFsPasswordTask newInstance(EncFsLocationBase container, Bundle passwordDialogResult)
    {
        Bundle args = new Bundle();
        args.putAll(passwordDialogResult);
        LocationsManager.storePathsInBundle(args, container, null);
        ChangeEncFsPasswordTask f = new ChangeEncFsPasswordTask();
        f.setArguments(args);
        return f;
	}

    @Override
	protected void changeLocationPassword() throws IOException, ApplicationException
    {
        EncFsLocationBase loc = (EncFsLocationBase)_location;
        SecureBuffer sb = Util.getPassword(getArguments(), LocationsManager.getLocationsManager(_context));
        byte[] pd = sb.getDataArray();
        try
        {

            loc.getEncFs().encryptVolumeKeyAndWriteConfig(pd);
        }
        finally
        {
            SecureBuffer.eraseData(pd);
            sb.close();
        }
	}
}
