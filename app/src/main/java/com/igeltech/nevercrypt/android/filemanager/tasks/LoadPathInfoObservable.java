package com.igeltech.nevercrypt.android.filemanager.tasks;

import com.igeltech.nevercrypt.android.helpers.CachedPathInfo;
import com.igeltech.nevercrypt.android.helpers.CachedPathInfoBase;
import com.igeltech.nevercrypt.locations.Location;

import io.reactivex.Single;

public class LoadPathInfoObservable
{
    public static Single<CachedPathInfo> create(Location loc)
    {
        return Single.create(emitter -> {
            CachedPathInfo cachedPathInfo = new CachedPathInfoBase();
            cachedPathInfo.init(loc.getCurrentPath());
            emitter.onSuccess(cachedPathInfo);
        });
    }
}
