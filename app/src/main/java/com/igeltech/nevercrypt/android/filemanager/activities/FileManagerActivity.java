package com.igeltech.nevercrypt.android.filemanager.activities;

import com.igeltech.nevercrypt.android.navigdrawer.DrawerController;
import com.igeltech.nevercrypt.locations.Location;

public class FileManagerActivity extends FileManagerActivityBase
{
    public static void openFileManager(FileManagerActivity fm, Location location, int scrollPosition)
    {
        fm.goTo(location, scrollPosition);
    }

    @Override
    protected DrawerController createDrawerController()
    {
        return new DrawerController(this);
    }
}
