package com.igeltech.nevercrypt.android.filemanager.records;

import android.view.View;
import android.view.ViewGroup;

import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.helpers.CachedPathInfo;
import com.igeltech.nevercrypt.android.helpers.ExtendedFileInfoLoader;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.locations.Location;

import java.io.IOException;

public interface BrowserRecord extends CachedPathInfo
{
    void init(Location location, Path path) throws IOException;
	String getName();
	boolean open() throws Exception;
    boolean openInplace() throws Exception;
    boolean allowSelect();
    boolean isSelected();
    void setSelected(boolean val);
	void setHostActivity(FileManagerActivity host);
    int getViewType();
    View createView(int position, ViewGroup parent);
    void updateView(View view, int position);
    void updateView();
    void setExtData(ExtendedFileInfoLoader.ExtendedFileInfo data);
    ExtendedFileInfoLoader.ExtendedFileInfo loadExtendedInfo();
    boolean needLoadExtendedInfo();
}
