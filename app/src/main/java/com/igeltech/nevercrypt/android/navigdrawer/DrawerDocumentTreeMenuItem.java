package com.igeltech.nevercrypt.android.navigdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.locations.Location;

public class DrawerDocumentTreeMenuItem extends DrawerLocationMenuItem
{
    private static Drawable _icon;

    public DrawerDocumentTreeMenuItem(Location location, DrawerControllerBase drawerController)
    {
        super(location, drawerController);
    }

    private synchronized static Drawable getIcon(Context context)
    {
        if (_icon == null)
        {
            _icon = context.getResources().getDrawable(R.drawable.ic_storage, context.getTheme());
        }
        return _icon;
    }

    @Override
    public Drawable getIcon()
    {
        return getIcon(getDrawerController().getMainActivity());
    }
}
