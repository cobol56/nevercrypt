package com.sovworks.eds.android.navigdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;

import com.sovworks.eds.android.R;
import com.sovworks.eds.android.filemanager.activities.FileManagerActivity;
import com.sovworks.eds.android.filemanager.fragments.FileListViewFragment;
import com.sovworks.eds.android.locations.closer.fragments.LocationCloserBaseFragment;
import com.sovworks.eds.android.locations.closer.fragments.OMLocationCloserFragment;
import com.sovworks.eds.android.locations.opener.fragments.EncFSOpenerFragment;
import com.sovworks.eds.android.locations.opener.fragments.LocationOpenerBaseFragment;
import com.sovworks.eds.locations.EDSLocation;
import com.sovworks.eds.locations.Location;

public class DrawerEncFsMenuItem extends DrawerLocationMenuItem
{
    public static class Opener extends EncFSOpenerFragment
    {
        @Override
        public void onLocationOpened(Location location)
        {
            Bundle args = getArguments();
            FileManagerActivity.openFileManager(
                    (FileManagerActivity)getActivity(),
                    location,
                    args != null ?
                            args.getInt(FileListViewFragment.ARG_SCROLL_POSITION, 0) :
                            0
            );
        }
    }

    @Override
    public Drawable getIcon()
    {
        return getLocation().isOpen() ?
                getOpenedIcon(getContext())
                :
                getClosedIcon(getContext());
    }

    @Override
    public EDSLocation getLocation()
    {
        return (EDSLocation) super.getLocation();
    }

    protected DrawerEncFsMenuItem(EDSLocation container, DrawerControllerBase drawerController)
    {
        super(container, drawerController);
    }

    @Override
    protected LocationOpenerBaseFragment getOpener()
    {
        return new Opener();
    }

    @Override
    protected LocationCloserBaseFragment getCloser()
    {
        return new OMLocationCloserFragment();
    }

    @Override
    protected boolean hasSettings()
    {
        return true;
    }

    private synchronized static Drawable getOpenedIcon(Context context)
    {
        if(_openedIcon == null)
        {
            _openedIcon = context.getResources().getDrawable(R.drawable.ic_lock_open, context.getTheme());
        }
        return _openedIcon;
    }

    private synchronized static Drawable getClosedIcon(Context context)
    {
        if(_closedIcon == null)
        {
            _closedIcon = context.getResources().getDrawable(R.drawable.ic_lock, context.getTheme());
        }
        return _closedIcon;
    }

    private static Drawable _openedIcon, _closedIcon;
}
