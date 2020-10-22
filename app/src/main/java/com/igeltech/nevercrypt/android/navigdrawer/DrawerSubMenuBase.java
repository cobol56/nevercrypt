package com.igeltech.nevercrypt.android.navigdrawer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.igeltech.nevercrypt.android.R;

import java.util.Collection;

public abstract class DrawerSubMenuBase extends DrawerMenuItemBase
{
    @Override
    public void onClick(View view, int position)
    {
        rotateIconAndChangeState(view);
    }

    @Override
    public void saveState(Bundle state)
    {
        if(isExpanded())
            state.putInt(STATE_EXPANDED_POSITION, getPositionInAdapter());
    }

    @Override
    public void restoreState(Bundle state)
    {
        int expPos = state.getInt(STATE_EXPANDED_POSITION, -1);
        if(expPos >= 0 && expPos == getPositionInAdapter())
            expand();
    }

    public boolean isExpanded()
    {
        return _isExpanded;
    }

    public boolean onBackPressed()
    {
        if(isExpanded())
        {
            collapse();
            return true;
        }
        return false;
    }

    @Override
    public void updateView(View view, int position)
    {
        super.updateView(view, position);
        AppCompatTextView tv = (AppCompatTextView)view.findViewById(android.R.id.text1);
        tv.setPressed(_isExpanded);
        Drawable drawable = view.getBackground();
        if(drawable!=null)
            drawable.setState(_isExpanded ? new int[] {android.R.attr.state_expanded} : new int[0]);
        AppCompatImageView iv = (AppCompatImageView) view.findViewById(android.R.id.icon);
        if(iv != null && !iv.hasTransientState())
        {
            iv.setVisibility(View.VISIBLE);
            iv.setRotation(isExpanded() ? 180 : 0);
        }
    }

    public void rotateIcon(View view)
    {
        final AppCompatImageView icon = (AppCompatImageView) view.findViewById(android.R.id.icon); //getIconImageView();
        if(icon!=null)
        {
            icon.clearAnimation();
            ObjectAnimator anim = ObjectAnimator.ofFloat(icon, View.ROTATION, isExpanded() ? 0 : 180);
            anim.setDuration(200);
            icon.setHasTransientState(true);
            anim.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    icon.setHasTransientState(false);
                }
            });
            anim.start();
        }
    }

    public void rotateIconAndChangeState(View view)
    {
        if(!isExpanded())
            rotateExpandedIcons();
        final AppCompatImageView icon = (AppCompatImageView) view.findViewById(android.R.id.icon); //getIconImageView();
        if(icon!=null)
        {
            IS_ANIMATING = true;
            icon.clearAnimation();
            ObjectAnimator anim = ObjectAnimator.ofFloat(icon, View.ROTATION, isExpanded() ? 0 : 180);
            anim.setDuration(200);
            icon.setHasTransientState(true);
            anim.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    if (isExpanded())
                        collapse();
                    else
                    {
                        collapseAll();
                        expand();
                    }
                    getAdapter().notifyDataSetChanged();
                    icon.setHasTransientState(false);
                    IS_ANIMATING = false;

                }
            });
            anim.start();
        }
        else
        {
            if(isExpanded())
                collapse();
            else
                expand();
        }
    }

    protected DrawerSubMenuBase(DrawerControllerBase drawerController)
    {
        super(drawerController);
    }

    protected abstract Collection<DrawerMenuItemBase> getSubItems();

    @Override
    protected int getLayoutId()
    {
        return R.layout.drawer_folder;
    }

    @Override
    public int getViewType()
    {
        return 1;
    }

    public View findView(ListView list)
    {
        for(int i = 0, n = list.getChildCount();i < n;i++)
        {
            View v = list.getChildAt(i);
            if (v.getTag() == this)
                return v;
        }
        /*int start = list.getFirstVisiblePosition();
        for (int i = start, j = list.getLastVisiblePosition(); i <= j; i++)
            if (this == list.getItemAtPosition(i))
                return list.getChildAt(i - start);*/
        return null;
    }
    protected void collapse()
    {
        _isExpanded = false;
        ArrayAdapter<DrawerMenuItemBase> adapter = getAdapter();
        if(_subItems != null)
        {
            for (DrawerMenuItemBase sub : _subItems)
                adapter.remove(sub);
        }
    }

    protected void expand()
    {
        _isExpanded = true;
        ArrayAdapter<DrawerMenuItemBase> adapter = getAdapter();
        _subItems = getSubItems();
        if(_subItems!=null)
        {
            int pos = getPositionInAdapter();
            if(pos >= 0)
                for (DrawerMenuItemBase sub : _subItems)
                    adapter.insert(sub, ++pos);
        }
    }

    public static boolean IS_ANIMATING = false;
    private static final String STATE_EXPANDED_POSITION = "com.igeltech.nevercrypt.android.navigdrawer.DrawerSubMenuBase.EXPANDED_POSITION";
    private boolean _isExpanded;



    private Collection<? extends DrawerMenuItemBase> _subItems;

    private void rotateExpandedIcons()
    {
        ListView lv = getDrawerController().getDrawerListView();
        for(int i=0;i<lv.getCount();i++)
        {
            Object di = lv.getItemAtPosition(i);
            if(di instanceof DrawerSubMenuBase && ((DrawerSubMenuBase)di).isExpanded())
            {
                View v = ((DrawerSubMenuBase)di).findView(lv);
                if(v!=null)
                    ((DrawerSubMenuBase) di).rotateIcon(v);
            }
        }
    }

    private void collapseAll()
    {
        ListView lv = getDrawerController().getDrawerListView();
        for(int i=0;i<lv.getCount();i++)
        {
            Object di = lv.getItemAtPosition(i);
            if(di instanceof DrawerSubMenuBase && ((DrawerSubMenuBase)di).isExpanded())
                ((DrawerSubMenuBase) di).collapse();

        }
    }

    /*private ImageView getIconImageView()
    {
        ListView list = getDrawerController().getDrawerListView();
        int start = list.getFirstVisiblePosition();
        for(int i=start, j=list.getLastVisiblePosition();i<=j;i++)
            if(this == list.getItemAtPosition(i))
            {
                View v = getAdapter().getView(i, list.getChildAt(i - start), list);
                if(v!=null)
                    return (ImageView) v.findViewById(android.R.id.icon);
            }
        return null;
    }*/


}
