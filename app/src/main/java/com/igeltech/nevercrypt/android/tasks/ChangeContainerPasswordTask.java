package com.igeltech.nevercrypt.android.tasks;

import android.os.Bundle;

import com.igeltech.nevercrypt.locations.ContainerLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class ChangeContainerPasswordTask extends ChangeContainerPasswordTaskBase
{
    public static ChangeContainerPasswordTask newInstance(ContainerLocation container, Bundle passwordDialogResult)
    {
        Bundle args = new Bundle();
        args.putAll(passwordDialogResult);
        LocationsManager.storePathsInBundle(args, container, null);
        ChangeContainerPasswordTask f = new ChangeContainerPasswordTask();
        f.setArguments(args);
        return f;
    }
}
