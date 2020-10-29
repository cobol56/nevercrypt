package com.igeltech.nevercrypt.android.navigdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.locations.opener.fragments.ExternalStorageOpenerFragment;
import com.igeltech.nevercrypt.android.locations.opener.fragments.LocationOpenerBaseFragment;
import com.igeltech.nevercrypt.locations.Location;

public class DrawerExternalSDMenuItem extends DrawerLocationMenuItem
{
    private static Drawable _icon;
    private final boolean _allowDocumentsAPI;

    DrawerExternalSDMenuItem(Location location, DrawerControllerBase drawerController, boolean allowDocumentsAPI)
    {
        super(location, drawerController);
        _allowDocumentsAPI = allowDocumentsAPI;
    }

    private synchronized static Drawable getIcon(Context context)
    {
        if (_icon == null)
        {
            _icon = context.getResources().getDrawable(R.drawable.ic_ext_storage, context.getTheme());
        }
        return _icon;
    }

    @Override
    public Drawable getIcon()
    {
        return getIcon(getDrawerController().getMainActivity());
    }

    @Override
    protected LocationOpenerBaseFragment getOpener()
    {
        return _allowDocumentsAPI ? new Opener() : super.getOpener();
    }

    public static class Opener extends ExternalStorageOpenerFragment
    {
        @Override
        public void onLocationOpened(Location location)
        {
            ((FileManagerActivity) getActivity()).goTo(location);
        }
    }
}
