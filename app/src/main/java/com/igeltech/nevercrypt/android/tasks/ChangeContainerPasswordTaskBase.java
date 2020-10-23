package com.igeltech.nevercrypt.android.tasks;

import android.os.Bundle;

import com.igeltech.nevercrypt.android.helpers.Util;
import com.igeltech.nevercrypt.container.VolumeLayout;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.File;
import com.igeltech.nevercrypt.fs.RandomAccessIO;
import com.igeltech.nevercrypt.locations.ContainerLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.locations.Openable;

import java.io.IOException;

public abstract class ChangeContainerPasswordTaskBase extends ChangeLocationPasswordTask
{
    public static final String TAG = "com.igeltech.nevercrypt.android.tasks.ChangeContainerPasswordTask";
    //public static final String ARG_FIN_ACTIVITY = "fin_activity";

	@Override
	protected void changeLocationPassword() throws IOException, ApplicationException
    {
        ContainerLocation cont = (ContainerLocation)_location;
        setContainerPassword(cont);
        RandomAccessIO io = cont.getLocation().getCurrentPath().getFile().getRandomAccessIO(File.AccessMode.ReadWrite);
        try
        {
            VolumeLayout vl = cont.getCryptoContainer().getVolumeLayout();
            vl.writeHeader(io);
        }
        finally
        {
            io.close();
        }
	}

	protected void setContainerPassword(ContainerLocation container) throws IOException
    {
        VolumeLayout vl = container.getCryptoContainer().getVolumeLayout();
        Bundle args  = getArguments();
        SecureBuffer sb = Util.getPassword(args, LocationsManager.getLocationsManager(_context));
        vl.setPassword(sb.getDataArray());
        sb.close();
        if(args.containsKey(Openable.PARAM_KDF_ITERATIONS))
            vl.setNumKDFIterations(args.getInt(Openable.PARAM_KDF_ITERATIONS));
    }
}
