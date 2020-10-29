package com.igeltech.nevercrypt.container;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import com.igeltech.nevercrypt.android.errors.UserException;
import com.igeltech.nevercrypt.android.filemanager.DirectorySettings;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.FileSystem;
import com.igeltech.nevercrypt.fs.util.StringPathUtil;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.settings.DefaultSettings;
import com.igeltech.nevercrypt.settings.Settings;

import java.io.IOException;
import java.util.Collections;

public abstract class LocationFormatterBase
{
    public static final String FORMAT_ENCFS = "EncFs";
    protected boolean _disableDefaultSettings, _dontReg;
    protected SecureBuffer _password;
    protected ProgressReporter _progressReporter;
    protected Context _context;

    public LocationFormatterBase()
    {
    }

    protected LocationFormatterBase(Parcel in)
    {
        _disableDefaultSettings = in.readByte() != 0;
        _password = in.readParcelable(ClassLoader.getSystemClassLoader());
    }

    public static String makeTitle(CryptoLocation cont, LocationsManager lm)
    {
        String startTitle;
        try
        {
            startTitle = new StringPathUtil(cont.getLocation().getCurrentPath().getPathDesc()).getFileNameWithoutExtension();
        }
        catch (IOException e)
        {
            startTitle = cont.getTitle();
        }
        return makeTitle(startTitle, lm, cont);
    }

    public static String makeTitle(String startTitle, LocationsManager lm, CryptoLocation ignore)
    {
        String title = startTitle;
        int i = 1;
        while (checkExistingTitle(title, lm, ignore))
            title = startTitle + " " + i++;
        return title;
    }

    private static boolean checkExistingTitle(String title, LocationsManager lm, CryptoLocation ignore)
    {
        Uri igUri = ignore.getLocation().getLocationUri();
        for (CryptoLocation cnt : lm.getLoadedCryptoLocations(true))
            if (cnt != ignore && !cnt.getLocation().getLocationUri().equals(igUri) && cnt.getTitle().equals(title))
                return true;
        return false;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeByte((byte) (_disableDefaultSettings ? 1 : 0));
        dest.writeParcelable(_password, 0);
    }

    public Context getContext()
    {
        return _context;
    }

    public void setContext(Context context)
    {
        _context = context;
    }

    public Settings getSettings()
    {
        return _context == null ? new DefaultSettings() : UserSettings.getSettings(_context);
    }

    public void setPassword(SecureBuffer pass)
    {
        _password = pass;
    }

    public void disableDefaultSettings(boolean val)
    {
        _disableDefaultSettings = val;
    }

    public void setProgressReporter(ProgressReporter reporter)
    {
        _progressReporter = reporter;
    }

    public CryptoLocation format(Location location) throws Exception
    {
        CryptoLocation loc = createLocation(location);
        if (!_dontReg)
            addLocationToList(loc);
        loc.getFS();
        initLocationSettings(loc);
        loc.close(false);
        if (!_dontReg)
            notifyLocationCreated(loc);
        return loc;
    }

    public void setDontRegLocation(boolean dontReg)
    {
        _dontReg = dontReg;
    }

    protected abstract CryptoLocation createLocation(Location location) throws IOException, ApplicationException, UserException;

    protected void addLocationToList(CryptoLocation loc) throws Exception
    {
        addLocationToList(loc, !UserSettings.getSettings(_context).neverSaveHistory());
    }

    protected void addLocationToList(CryptoLocation loc, boolean store) throws Exception
    {
        LocationsManager lm = LocationsManager.getLocationsManager(_context, true);
        if (lm != null)
        {
            Location prevLoc = lm.findExistingLocation(loc);
            if (prevLoc == null)
                lm.addNewLocation(loc, store);
            else
                lm.replaceLocation(prevLoc, loc, store);
        }
    }

    protected void notifyLocationCreated(CryptoLocation loc)
    {
        if (_context != null)
            LocationsManager.broadcastLocationAdded(_context, loc);
    }

    protected void initLocationSettings(CryptoLocation loc) throws IOException, ApplicationException
    {
        writeInternalContainerSettings(loc);
        if (!_dontReg)
            setExternalContainerSettings(loc);
    }

    protected void setExternalContainerSettings(CryptoLocation loc) throws ApplicationException, IOException
    {
        LocationsManager lm = LocationsManager.getLocationsManager(_context, true);
        String title = makeTitle(loc, lm);
        loc.getExternalSettings().setTitle(title);
        loc.getExternalSettings().setVisibleToUser(true);
        if (_context == null || !UserSettings.getSettings(_context).neverSaveHistory())
            loc.saveExternalSettings();
    }

    protected void writeInternalContainerSettings(CryptoLocation loc) throws IOException
    {
        if (_disableDefaultSettings)
            return;
        DirectorySettings ds = new DirectorySettings();
        ds.setHiddenFilesMasks(Collections.singletonList("(?iu)\\.crypt.*"));
        FileSystem fs = loc.getFS();
        ds.saveToDir(fs.getRootPath().getDirectory());
    }

    protected boolean reportProgress(byte prc)
    {
        return _progressReporter == null || _progressReporter.report(prc);
    }

    public interface ProgressReporter
    {
        boolean report(byte prc);
    }
}
