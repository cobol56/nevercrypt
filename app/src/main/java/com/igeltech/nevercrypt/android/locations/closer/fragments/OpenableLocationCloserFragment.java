package com.igeltech.nevercrypt.android.locations.closer.fragments;


import android.content.Context;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.helpers.TempFilesMonitor;
import com.igeltech.nevercrypt.android.helpers.WipeFilesTask;
import com.igeltech.nevercrypt.android.providers.ContainersDocumentProviderBase;
import com.igeltech.nevercrypt.android.service.FileOpsService;
import com.igeltech.nevercrypt.android.service.LocationsService;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.fs.util.SrcDstRec;
import com.igeltech.nevercrypt.fs.util.SrcDstSingle;
import com.igeltech.nevercrypt.locations.EDSLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.locations.Openable;

import java.io.IOException;

public class OpenableLocationCloserFragment extends LocationCloserBaseFragment
{
    public static void wipeMirror(Context context, Location location) throws IOException
    {
        Location mirrorLocation = FileOpsService.getMirrorLocation(
                UserSettings.getSettings(context).getWorkDir(),
                context,
                location.getId()
        );
        if(mirrorLocation.getCurrentPath().exists())
        {
            SrcDstRec sdr = new SrcDstRec(new SrcDstSingle(
                    mirrorLocation,
                    null
            )
            );
            sdr.setIsDirLast(true);
            WipeFilesTask.wipeFilesRnd(
                    null,
                    TempFilesMonitor.getMonitor(context).getSyncObject(),
                    true,
                    sdr
            );
        }
    }

    public static void closeLocation(Context context, Openable location, boolean forceClose) throws IOException
    {
        try
        {
            location.close(forceClose);
        }
        catch (Exception e)
        {
            if(forceClose)
                Logger.log(e);
            else
                throw e;
        }
        makePostCloseCheck(context, location);
        wipeMirror(context, location);
    }

    public static void makePostCloseCheck(Context context, Location loc)
    {
        if(loc instanceof Openable && LocationsManager.isOpen(loc))
            return;
        LocationsManager lm = LocationsManager.getLocationsManager(context);
        LocationsManager.broadcastLocationChanged(context, loc);
        lm.unregOpenedLocation(loc);
        if(loc instanceof EDSLocation)
            ContainersDocumentProviderBase.notifyOpenedLocationsListChanged(context);

        if(!lm.hasOpenLocations())
        {
            lm.broadcastAllContainersClosed();
            LocationsService.stopService(context);
        }
    }

    public static class CloseLocationTaskFragment extends LocationCloserBaseFragment.CloseLocationTaskFragment
    {
        @Override
        protected void procLocation(TaskState state, Location location) throws Exception
        {
            boolean fc = getArguments().getBoolean(ARG_FORCE_CLOSE, UserSettings.getSettings(_context).alwaysForceClose());
            try
            {
                closeLocation(_context, (Openable)location, fc);
            }
            catch (Exception e)
            {
                if(fc)
                    Logger.log(e);
                else
                    throw e;
            }
        }
    }

    @Override
    protected TaskFragment getCloseLocationTask()
    {
        return new CloseLocationTaskFragment();
    }
}
