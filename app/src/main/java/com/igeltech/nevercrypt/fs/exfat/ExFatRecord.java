package com.igeltech.nevercrypt.fs.exfat;

import com.igeltech.nevercrypt.fs.Directory;
import com.igeltech.nevercrypt.fs.FSRecord;
import com.igeltech.nevercrypt.fs.util.StringPathUtil;

import java.io.IOException;
import java.util.Date;


abstract class ExFatRecord implements FSRecord
{
    ExFatRecord(ExFat exFat, ExFatPath path)
    {
        _exFat = exFat;
        _path = path;
    }

    @Override
    public ExFatPath getPath()
    {
        return _path;
    }

    @Override
    public String getName() throws IOException
    {
        return _path.getPathUtil().getFileName();
    }

    @Override
    public void rename(String newName) throws IOException
    {
        StringPathUtil oldPath = getPath().getPathUtil();
        StringPathUtil newPath = oldPath.getParentPath().combine(newName);
        int res = _exFat.rename(oldPath.toString(), newPath.toString());
        if(res != 0)
            throw new IOException("Rename failed. Error code = " + res);
        _path = new ExFatPath(_exFat, newPath.toString());
    }

    @Override
    public Date getLastModified() throws IOException
    {
        return new Date(_path.getAttr().modTime*1000);
    }

    @Override
    public void setLastModified(Date dt) throws IOException
    {
        _exFat.updateTime(_path.getPathString(), dt.getTime());
    }

    @Override
    public void moveTo(Directory newParent) throws IOException
    {
        StringPathUtil oldPath = getPath().getPathUtil();
        StringPathUtil newPath = ((ExFatDirectory)newParent).getPath().getPathUtil().combine(oldPath.getFileName());
        int res = _exFat.rename(oldPath.toString(), newPath.toString());
        if(res != 0)
            throw new IOException("moveTo failed. Error code = " + res);
        _path = new ExFatPath(_exFat, newPath.toString());
    }

    final ExFat _exFat;
    protected ExFatPath _path;
}
