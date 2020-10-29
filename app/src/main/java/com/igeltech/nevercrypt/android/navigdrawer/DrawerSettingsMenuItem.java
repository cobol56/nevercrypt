package com.igeltech.nevercrypt.android.navigdrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.activities.ProgramSettingsActivity;

public class DrawerSettingsMenuItem extends DrawerMenuItemBase
{
    public DrawerSettingsMenuItem(DrawerControllerBase drawerController)
    {
        super(drawerController);
    }

    @Override
    public String getTitle()
    {
        return getDrawerController().getMainActivity().getString(R.string.settings);
    }

    @Override
    public void onClick(View view, int position)
    {
        super.onClick(view, position);
        getContext().startActivity(new Intent(getContext(), ProgramSettingsActivity.class));
    }

    @Override
    public Drawable getIcon()
    {
        return getIcon(getDrawerController().getMainActivity());
    }

    private synchronized static Drawable getIcon(Context context)
    {
        if (_icon == null)
        {
            _icon = context.getResources().getDrawable(R.drawable.ic_settings, context.getTheme());
        }
        return _icon;
    }

    private static Drawable _icon;
}
