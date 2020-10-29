package com.igeltech.nevercrypt.fs.util;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.filemanager.DirectorySettings;
import com.igeltech.nevercrypt.android.filemanager.tasks.ReadDir;
import com.igeltech.nevercrypt.fs.FileSystem;

import java.io.IOException;
import java.util.HashMap;

public class ContainerFSWrapper extends ActivityTrackingFSWrapper
{
    private final HashMap<com.igeltech.nevercrypt.fs.Path, DirectorySettings> _dirSettingsCache = new HashMap<>();

    public ContainerFSWrapper(FileSystem baseFs)
    {
        super(baseFs);
    }

    @Override
    public void setChangesListener(ChangeListener listener)
    {
        throw new UnsupportedOperationException();
    }

    public synchronized DirectorySettings getDirectorySettings(com.igeltech.nevercrypt.fs.Path path)
    {
        if (path == null)
            return null;
        if (_dirSettingsCache.containsKey(path))
            return _dirSettingsCache.get(path);
        DirectorySettings ds = null;
        try
        {
            ds = ReadDir.loadDirectorySettings(path);
        }
        catch (IOException e)
        {
            Logger.log(e);
        }
        _dirSettingsCache.put(path, ds);
        return ds;
    }
}
