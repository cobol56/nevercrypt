package com.igeltech.nevercrypt.android.filemanager.tasks;

import android.content.Context;

import com.igeltech.nevercrypt.android.filemanager.DirectorySettings;
import com.igeltech.nevercrypt.android.filemanager.records.BrowserRecord;
import com.igeltech.nevercrypt.android.filemanager.records.ExecutableFileRecord;
import com.igeltech.nevercrypt.android.filemanager.records.FolderRecord;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.fs.util.StringPathUtil;
import com.igeltech.nevercrypt.locations.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ReadDir extends ReadDirBase
{
    static BrowserRecord createBrowserRecordFromFile(Context context, Location loc, Path path, DirectorySettings directorySettings) throws IOException
    {
        if (directorySettings != null)
        {
            StringPathUtil pu;
            if (path.isFile())
                pu = new StringPathUtil(path.getFile().getName());
            else if (path.isDirectory())
                pu = new StringPathUtil(path.getDirectory().getName());
            else
                pu = new StringPathUtil(path.getPathString());
            ArrayList<String> masks = directorySettings.getHiddenFilesMasks();
            if (masks != null)
                for (String mask : masks)
                {
                    if (pu.getFileName().matches(mask))
                        return null;
                }
        }

        return path.isDirectory() ? new FolderRecord(context) : new ExecutableFileRecord(context);
    }

    ReadDir(Context context, Location targetLocation, Collection<Path> selectedFiles, DirectorySettings dirSettings, boolean showRootFolderLink)
    {
        super(context, targetLocation, selectedFiles, dirSettings, showRootFolderLink);
    }
}
