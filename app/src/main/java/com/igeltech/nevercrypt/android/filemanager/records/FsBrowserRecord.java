package com.igeltech.nevercrypt.android.filemanager.records;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;
import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.filemanager.fragments.FileListViewFragment;
import com.igeltech.nevercrypt.android.filemanager.fragments.FileManagerFragment;
import com.igeltech.nevercrypt.android.helpers.CachedPathInfoBase;
import com.igeltech.nevercrypt.android.helpers.ExtendedFileInfoLoader;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.locations.Location;

import java.io.IOException;

public abstract class FsBrowserRecord extends CachedPathInfoBase implements BrowserRecord
{
    protected final Context _context;
    protected FileManagerFragment _host;
    private boolean _isSelected;

    public FsBrowserRecord(Context context)
    {
        _context = context;
    }

    public static void updateRowView(FileManagerFragment host, Object item)
    {
        updateRowView((FileListViewFragment) host.getChildFragmentManager().findFragmentByTag(FileListViewFragment.TAG), item);
    }

    public static void updateRowView(FileListViewFragment host, Object item)
    {
        RowViewInfo rvi = getCurrentRowViewInfo(host, item);
        if (rvi != null)
            updateRowView(rvi);
    }

    public static void updateRowView(RowViewInfo rvi)
    {
        rvi.listView.getAdapter().getView(rvi.position, rvi.view, rvi.listView);
    }

    public static RowViewInfo getCurrentRowViewInfo(FileListViewFragment host, Object item)
    {
        if (host == null || host.isRemoving() || !host.isResumed())
            return null;
        ListView list = host.getListView();
        if (list == null)
            return null;
        int start = list.getFirstVisiblePosition();
        for (int i = start, j = list.getLastVisiblePosition(); i <= j; i++)
            if (j < list.getCount() && item == list.getItemAtPosition(i))
            {
                RowViewInfo rvi = new RowViewInfo();
                rvi.view = list.getChildAt(i - start);
                rvi.position = i;
                rvi.listView = list;
                return rvi;
            }
        return null;
    }

    public static RowViewInfo getCurrentRowViewInfo(FileManagerActivity host, Object item)
    {
        if (host == null)
            return null;
        FileListViewFragment f = (FileListViewFragment) host.getSupportFragmentManager().findFragmentByTag(FileListViewFragment.TAG);
        return getCurrentRowViewInfo(f, item);
    }

    @Override
    public int getViewType()
    {
        return 0;
    }

    public boolean isSelected()
    {
        return _isSelected;
    }

    @Override
    public void setSelected(boolean val)
    {
        _isSelected = val;
    }

    @Override
    public View createView(int position, ViewGroup parent)
    {
        if (_host == null || !_host.isAdded())
            return null;
        LayoutInflater inflater = _host.getLayoutInflater();
        View v = inflater.inflate(R.layout.fs_browser_row, parent, false);
        ((ViewGroup) v).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        updateView(v, position);
        return v;
    }

    @Override
    public void updateView(View view, final int position)
    {
        final FileListViewFragment hf = getHostFragment();
        AppCompatCheckBox cb = view.findViewById(android.R.id.checkbox);
        if (cb != null)
        {
            if (allowSelect() && (_host.isSelectAction() || hf.isInSelectionMode()) && (!_host.isSelectAction() || !_host.isSingleSelectionMode()))
            {
                cb.setOnCheckedChangeListener(null);
                cb.setChecked(isSelected());
                cb.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                    if (isChecked)
                        hf.selectFile(FsBrowserRecord.this);
                    else
                        hf.unselectFile(FsBrowserRecord.this);
                });
                cb.setVisibility(View.VISIBLE);
            }
            else
                cb.setVisibility(View.INVISIBLE);
        }
        MaterialRadioButton rb = view.findViewById(R.id.radio);
        if (rb != null)
        {
            if (allowSelect() && _host.isSelectAction() && _host.isSingleSelectionMode())
            {
                rb.setOnCheckedChangeListener(null);
                rb.setChecked(isSelected());
                rb.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                    if (isChecked)
                        hf.selectFile(FsBrowserRecord.this);
                    else
                        hf.unselectFile(FsBrowserRecord.this);
                });
                rb.setVisibility(View.VISIBLE);
            }
            else
                rb.setVisibility(View.INVISIBLE);
        }
        MaterialTextView tv = view.findViewById(android.R.id.text1);
        tv.setText(getName());
        AppCompatImageView iv = view.findViewById(android.R.id.icon);
        iv.setImageDrawable(getDefaultIcon());
        iv.setScaleType(AppCompatImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(view1 -> {
            if (allowSelect())
            {
                if (isSelected())
                {
                    if (!_host.isSelectAction() || !_host.isSingleSelectionMode())
                        hf.unselectFile(FsBrowserRecord.this);
                }
                else
                    hf.selectFile(FsBrowserRecord.this);
            }
        });
        iv = view.findViewById(android.R.id.icon1);
        iv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateView()
    {
        if (_host.isAdded())
            updateRowView(_host, this);
        else
            Logger.debug("FsBrowserRecord updateView - host not attached");
    }

    @Override
    public void setExtData(ExtendedFileInfoLoader.ExtendedFileInfo data)
    {
    }

    @Override
    public ExtendedFileInfoLoader.ExtendedFileInfo loadExtendedInfo()
    {
        return null;
    }

    @Override
    public boolean allowSelect()
    {
        return true;
    }

    @Override
    public boolean open() throws Exception
    {
        return false;
    }

    @Override
    public boolean openInplace() throws Exception
    {
        return false;
    }

    @Override
    public boolean needLoadExtendedInfo()
    {
        return false;
    }

    @Override
    public void init(Location location, Path path) throws IOException
    {
        init(path);
    }

    protected abstract Drawable getDefaultIcon();

    protected FileListViewFragment getHostFragment()
    {
        if (_host != null && !_host.isAdded())
            Logger.debug("FsBrowserRecord getHostFragment - host not attached");
        return _host != null && _host.isAdded() ? (FileListViewFragment) _host.getChildFragmentManager().findFragmentByTag(FileListViewFragment.TAG) : null;
    }

    @Override
    public void setHostFragment(FileManagerFragment host)
    {
        _host = host;
    }

    public static class RowViewInfo
    {
        public ListView listView;
        public View view;
        public int position;
    }
}
