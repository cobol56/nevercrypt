package com.igeltech.nevercrypt.android.helpers;

import com.igeltech.nevercrypt.fs.Directory;
import com.igeltech.nevercrypt.fs.File;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.fs.util.StringPathUtil;

import java.io.IOException;
import java.util.Date;

public class CachedPathInfoBase implements CachedPathInfo
{
    protected Path _path;
    protected boolean _isFile, _isDirectory;
    private Date _modDate;
    private long _size;
    private String _pathDesc, _name;

    @Override
    public Path getPath()
    {
        return _path;
    }

    @Override
    public String getPathDesc()
    {
        return _pathDesc;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    @Override
    public boolean isFile()
    {
        return _isFile;
    }

    @Override
    public boolean isDirectory()
    {
        return _isDirectory;
    }

    @Override
    public Date getModificationDate()
    {
        return _modDate;
    }

    @Override
    public long getSize()
    {
        return _size;
    }

    @Override
    public void init(Path path) throws IOException
    {
        _path = path;
        if (_path != null)
            updateCommonPathParams();
    }

    public void updateCommonPathParams()
    {
        try
        {
            _pathDesc = _path.getPathDesc();
            _isFile = _path.isFile();
            _isDirectory = _path.isDirectory();
            if (_isFile)
            {
                File f = _path.getFile();
                _modDate = f.getLastModified();
                _size = f.getSize();
                _name = f.getName();
            }
            else if (_isDirectory)
            {
                Directory dir = _path.getDirectory();
                _modDate = dir.getLastModified();
                _name = dir.getName();
            }
            else
                _name = new StringPathUtil(_pathDesc).getFileName();
        }
        catch (IOException ignored)
        {
        }
    }
}
