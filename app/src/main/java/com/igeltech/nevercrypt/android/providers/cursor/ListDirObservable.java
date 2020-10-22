package com.igeltech.nevercrypt.android.providers.cursor;


import android.net.Uri;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.helpers.CachedPathInfo;
import com.igeltech.nevercrypt.android.helpers.CachedPathInfoBase;
import com.igeltech.nevercrypt.fs.Directory;
import com.igeltech.nevercrypt.fs.FSRecord;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposables;

public class ListDirObservable
{

    public static Observable<CachedPathInfo> create(LocationsManager lm, Uri locUri)
    {
        return Observable.create(observableEmitter -> {
            Location loc = lm.getLocation(locUri);
            if(loc.getCurrentPath().isDirectory())
                emitListDir(loc.getCurrentPath().getDirectory(), observableEmitter);
            else if(loc.getCurrentPath().isFile())
                emitFile(loc.getCurrentPath().getFile(), observableEmitter);
            else
                observableEmitter.onComplete();
        });
    }

    public static Observable<CachedPathInfo> create(Location loc, boolean listDir)
    {
        return Observable.create(observableEmitter -> {
            if(loc.getCurrentPath().isDirectory())
            {
                if(listDir)
                    emitListDir(loc.getCurrentPath().getDirectory(), observableEmitter);
                else
                    emitFile(loc.getCurrentPath().getDirectory(), observableEmitter);
            }
            else if(loc.getCurrentPath().isFile())
                emitFile(loc.getCurrentPath().getFile(), observableEmitter);
            else
                observableEmitter.onComplete();
        });
    }

    private static void emitFile(FSRecord f, ObservableEmitter<CachedPathInfo> observableEmitter) throws IOException
    {
        CachedPathInfo cpi = new CachedPathInfoBase();
        cpi.init(f.getPath());
        observableEmitter.onNext(cpi);
        observableEmitter.onComplete();
    }

    private static void emitListDir(Directory dir, ObservableEmitter<CachedPathInfo> observableEmitter) throws IOException
    {
        Directory.Contents contents = dir.list();
        observableEmitter.setDisposable(Disposables.fromRunnable(() ->
        {
            try
            {
                contents.close();
            }
            catch (IOException e)
            {
                Logger.log(e);
            }
        }));
        for(Path p: contents)
        {
            CachedPathInfo cpi = new CachedPathInfoBase();
            try
            {
                cpi.init(p);
                observableEmitter.onNext(cpi);
            }
            catch (IOException e)
            {
                Logger.log(e);
            }
        }
        observableEmitter.onComplete();
    }
}
