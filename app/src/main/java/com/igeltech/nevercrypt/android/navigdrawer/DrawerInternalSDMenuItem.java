package com.igeltech.nevercrypt.android.navigdrawer;


import android.content.Context;
import android.graphics.drawable.Drawable;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.locations.Location;

public class DrawerInternalSDMenuItem extends DrawerLocationMenuItem
{
    public DrawerInternalSDMenuItem(Location location, DrawerControllerBase drawerController)
    {
        super(location, drawerController);
    }

    @Override
    public Drawable getIcon()
    {
        return getIcon(getDrawerController().getMainActivity());
    }

    private synchronized static Drawable getIcon(Context context)
    {
        if(_icon == null)
        {
            _icon = context.getResources().getDrawable(R.drawable.ic_device_memory, context.getTheme());
        }
        return _icon;
    }

    private static Drawable _icon;
}
