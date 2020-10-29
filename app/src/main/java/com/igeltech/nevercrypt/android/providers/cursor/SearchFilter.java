package com.igeltech.nevercrypt.android.providers.cursor;

import com.igeltech.nevercrypt.android.helpers.CachedPathInfo;
import com.igeltech.nevercrypt.locations.Location;

import io.reactivex.functions.Predicate;

public interface SearchFilter
{
    String getName();

    Predicate<CachedPathInfo> getChecker(Location location, String arg);
}
