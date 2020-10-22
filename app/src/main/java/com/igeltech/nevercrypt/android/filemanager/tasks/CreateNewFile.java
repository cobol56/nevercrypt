package com.igeltech.nevercrypt.android.filemanager.tasks;


import android.content.Context;

import com.igeltech.nevercrypt.locations.Location;

public class CreateNewFile extends CreateNewFileBase
{
    CreateNewFile(Context context, Location location, String fileName, int fileType, boolean returnExisting)
    {
        super(context, location, fileName, fileType, returnExisting);
    }
}
