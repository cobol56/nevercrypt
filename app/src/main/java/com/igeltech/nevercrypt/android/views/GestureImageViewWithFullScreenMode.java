package com.igeltech.nevercrypt.android.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

public class GestureImageViewWithFullScreenMode extends GestureImageView// implements android.view.View.OnSystemUiVisibilityChangeListener
{
    private final Runnable _navHider = () -> setNavVisibility(false);

    /* @Override
     public void onSystemUiVisibilityChange(int visibility)
     {
         // Detect when we go out of low-profile mode, to also go out
         // of full screen.  We only do this when the low profile mode
         // is changing from its last state, and turning off.
         int diff = _lastSystemUiVis ^ visibility;
         _lastSystemUiVis = visibility;
         if ((diff&SYSTEM_UI_FLAG_LOW_PROFILE) != 0 && (visibility&SYSTEM_UI_FLAG_LOW_PROFILE) == 0)
             setNavVisibility(true);
     }*/
    private boolean _isFullScreenMode;

    public GestureImageViewWithFullScreenMode(Context context, AttributeSet attr)
    {
        super(context, attr);
        //setOnSystemUiVisibilityChangeListener(this);
    }

    public void setFullscreenMode(boolean activate)
    {
        if (activate)
        {
            _isFullScreenMode = true;
            setNavVisibility(false);
        }
        else
        {
            _isFullScreenMode = false;
            Handler h = getHandler();
            if (h != null)
                h.removeCallbacks(_navHider);
            setSystemUiVisibility(0);
        }
    }

    private void setNavVisibility(boolean visible)
    {
        int newVis = SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (!visible)
            newVis |= SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        final boolean changed = newVis == getSystemUiVisibility();
        // Unschedule any pending event to hide navigation if we are
        // changing the visibility, or making the UI visible.
        if (changed || visible)
        {
            Handler h = getHandler();
            if (h != null)
                h.removeCallbacks(_navHider);
        }
        // Set the new desired visibility.
        setSystemUiVisibility(newVis);
    }

    @Override
    protected void onTouchUp()
    {
        super.onTouchUp();
        if (_isFullScreenMode)
        {
            setNavVisibility(true);
            delayedFullScreen();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility)
    {
        super.onWindowVisibilityChanged(visibility);
        if (_isFullScreenMode)
        {
            setNavVisibility(true);
            delayedFullScreen();
        }
    }

    private void delayedFullScreen()
    {
        // When we become visible, we show our navigation elements briefly
        // before hiding them.
        getHandler().postDelayed(_navHider, 2000);
    }
}
