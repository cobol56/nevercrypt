package com.igeltech.nevercrypt.android.locations.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.fs.DocumentTreeFS;
import com.igeltech.nevercrypt.android.locations.DocumentTreeLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DocumentTreeLocationsListFragment extends LocationListBaseFragment
{
    private static final int REQUEST_CODE_ADD_LOCATION = AppCompatActivity.RESULT_FIRST_USER;
    private static Drawable _icon;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_ADD_LOCATION)
        {
            if (resultCode == AppCompatActivity.RESULT_OK)
            {
                Uri treeUri = data.getData();
                try
                {
                    getActivity().getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                catch (SecurityException e)
                {
                    Logger.log(e);
                }
                DocumentTreeLocation loc = new DocumentTreeLocation(getActivity().getApplicationContext(), treeUri);
                loc.getExternalSettings().setVisibleToUser(true);
                loc.saveExternalSettings();
                LocationsManager.getLocationsManager(getActivity()).addNewLocation(loc, true);
                LocationsManager.broadcastLocationAdded(getActivity(), loc);
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void removeLocation(Location loc)
    {
        DocumentTreeLocation tl = (DocumentTreeLocation) loc;
        try
        {
            DocumentTreeFS.DocumentPath p = (DocumentTreeFS.DocumentPath) tl.getFS().getRootPath();
            getActivity().getContentResolver().releasePersistableUriPermission(p.getDocumentUri(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        catch (SecurityException e)
        {
            Logger.log(e);
        }
        catch (Exception e)
        {
            Logger.showAndLog(getActivity(), e);
        }
        super.removeLocation(loc);
    }

    @Override
    protected void loadLocations()
    {
        _locationsList.clear();
        for (Location loc : LocationsManager.getLocationsManager(getActivity()).getLoadedLocations(true))
            if (loc instanceof DocumentTreeLocation)
            {
                LocationInfo li = new LocationInfo((DocumentTreeLocation) loc);
                _locationsList.add(li);
            }
    }

    @Override
    protected String getDefaultLocationType()
    {
        return DocumentTreeLocation.URI_SCHEME;
    }

    @Override
    protected void addNewLocation(String locationType)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_ADD_LOCATION);
    }

    private synchronized Drawable getLoadedIcon()
    {
        if (_icon == null)
        {
            _icon = getResources().getDrawable(R.drawable.ic_ext_storage);
        }
        return _icon;
    }

    private class LocationInfo extends LocationListBaseFragment.LocationInfo
    {
        public LocationInfo(DocumentTreeLocation l)
        {
            location = l;
        }

        @Override
        public Drawable getIcon()
        {
            return getLoadedIcon();
        }
    }
}
