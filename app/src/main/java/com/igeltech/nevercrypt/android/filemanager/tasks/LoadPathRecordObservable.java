package com.igeltech.nevercrypt.android.filemanager.tasks;

import android.content.Context;

import com.igeltech.nevercrypt.android.filemanager.DirectorySettings;
import com.igeltech.nevercrypt.android.filemanager.records.BrowserRecord;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.locations.Location;

import io.reactivex.Single;

public class LoadPathRecordObservable
{
    public static Single<BrowserRecord> create(Context context, Location targetLocation, DirectorySettings dirSettings)
    {
        return Single.create(s -> {
            Path p = targetLocation.getCurrentPath();
            s.onSuccess(ReadDir.getBrowserRecordFromFsRecord(context, targetLocation, p, dirSettings));
        });
    }
}
