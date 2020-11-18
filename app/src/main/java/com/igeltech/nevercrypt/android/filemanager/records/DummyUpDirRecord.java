package com.igeltech.nevercrypt.android.filemanager.records;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.fragments.FileListDataFragment;

import java.io.IOException;
import java.util.Stack;

public class DummyUpDirRecord extends FolderRecord
{
    private static Drawable _icon;

    public DummyUpDirRecord(Context context) throws IOException
    {
        super(context);
    }

    private static synchronized Drawable getIcon(Context context)
    {
        if (_icon == null && context != null)
        {
            _icon = context.getDrawable(R.drawable.ic_folder_up);
        }
        return _icon;
    }

    @Override
    public String getName()
    {
        return "..";
    }

    @Override
    public boolean allowSelect()
    {
        return false;
    }

    @Override
    public boolean isFile()
    {
        return false;
    }

    @Override
    public boolean isDirectory()
    {
        return true;
    }

    @Override
    public boolean open() throws Exception
    {
        super.open();
        Stack<FileListDataFragment.HistoryItem> nh = _host.getFileListDataFragment().getNavigHistory();
        if (!nh.empty())
            nh.pop();
        return true;
    }

    @Override
    protected Drawable getDefaultIcon()
    {
        return getIcon(_host.getContext());
    }
}