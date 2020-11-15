package com.igeltech.nevercrypt.android.providers.cursor;

import com.igeltech.nevercrypt.android.helpers.CachedPathInfo;
import com.igeltech.nevercrypt.locations.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.reactivex.functions.Predicate;

class SelectionCheckerBase implements Predicate<CachedPathInfo>
{
    private static final SearchFilter[] ALL_FILTERS = new SearchFilter[]{};
    protected final Location _location;
    final List<Predicate<CachedPathInfo>> _filters = new ArrayList<>();

    SelectionCheckerBase(Location location, String selectionString, String[] selectionArgs)
    {
        _location = location;
        if (selectionString != null)
        {
            String[] filtNames = selectionString.split(" ");
            int i = 0;
            for (String filtName : filtNames)
            {
                if (selectionArgs == null || i >= selectionArgs.length)
                    break;
                Predicate<CachedPathInfo> f = getFilter(filtName, selectionArgs[i++]);
                if (f != null)
                    _filters.add(f);
            }
        }
    }

    @Override
    public boolean test(CachedPathInfo cachedPathInfo) throws Exception
    {
        for (Predicate<CachedPathInfo> pc : _filters)
            if (!pc.test(cachedPathInfo))
                return false;
        return true;
    }

    protected Collection<SearchFilter> getAllFilters()
    {
        return Arrays.asList(ALL_FILTERS);
    }

    private Predicate<CachedPathInfo> getFilter(String filtName, String arg)
    {
        for (SearchFilter f : getAllFilters())
            if (f.getName().equals(filtName))
                return f.getChecker(_location, arg);
        return null;
    }
}
