package com.igeltech.nevercrypt.android.navigdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.filemanager.fragments.FileListViewFragment;
import com.igeltech.nevercrypt.android.locations.closer.fragments.LocationCloserBaseFragment;
import com.igeltech.nevercrypt.android.locations.closer.fragments.OMLocationCloserFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.ContainerOpenerFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.LocationOpenerBaseFragment;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Location;

public class DrawerContainerMenuItem extends DrawerLocationMenuItem
{
    private static Drawable _openedIcon, _closedIcon;

    protected DrawerContainerMenuItem(CryptoLocation container, DrawerControllerBase drawerController)
    {
        super(container, drawerController);
    }

    private synchronized static Drawable getOpenedIcon(Context context)
    {
        if (_openedIcon == null)
        {
            _openedIcon = context.getResources().getDrawable(R.drawable.ic_lock_open, context.getTheme());
        }
        return _openedIcon;
    }

    private synchronized static Drawable getClosedIcon(Context context)
    {
        if (_closedIcon == null)
        {
            _closedIcon = context.getResources().getDrawable(R.drawable.ic_lock, context.getTheme());
        }
        return _closedIcon;
    }

    @Override
    public Drawable getIcon()
    {
        return getLocation().isOpenOrMounted() ? getOpenedIcon(getContext()) : getClosedIcon(getContext());
    }

    @Override
    public CryptoLocation getLocation()
    {
        return (CryptoLocation) super.getLocation();
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

    public static class Opener extends ContainerOpenerFragment
    {
        @Override
        public void onLocationOpened(Location location)
        {
            if (location.isFileSystemOpen())
            {
                Bundle args = getArguments();
                FileManagerActivity.openFileManager((FileManagerActivity) getActivity(), location, args != null ? args.getInt(FileListViewFragment.ARG_SCROLL_POSITION, 0) : 0);
            }
        }
    }
}
