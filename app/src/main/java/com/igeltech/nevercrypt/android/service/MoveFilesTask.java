package com.igeltech.nevercrypt.android.service;

import android.content.Intent;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.fragments.FileManagerFragment;
import com.igeltech.nevercrypt.android.helpers.ExtendedFileInfoLoader;
import com.igeltech.nevercrypt.fs.Directory;
import com.igeltech.nevercrypt.fs.FSRecord;
import com.igeltech.nevercrypt.fs.File;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.fs.errors.NoFreeSpaceLeftException;
import com.igeltech.nevercrypt.fs.util.SrcDstCollection;
import com.igeltech.nevercrypt.fs.util.SrcDstCollection.SrcDst;
import com.igeltech.nevercrypt.locations.Location;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MoveFilesTask extends CopyFilesTask
{
    private final List<Directory> _foldersToDelete = new ArrayList<>();
    private boolean _wipe;

    @Override
    protected String getErrorMessage(Throwable ex)
    {
        return _context.getString(R.string.move_failed);
    }

    @Override
    protected Intent getOverwriteRequestIntent(SrcDstCollection filesToOverwrite) throws IOException, JSONException
    {
        return FileManagerFragment.getOverwriteRequestIntent(_context, true, filesToOverwrite);
    }

    @Override
    protected void processSrcDstCollection(SrcDstCollection col) throws Exception
    {
        super.processSrcDstCollection(col);
        for (Directory dir : _foldersToDelete)
            deleteEmptyDir(dir);
    }

    @Override
    protected boolean processRecord(SrcDst record) throws Exception
    {
        try
        {
            Location srcLocation = record.getSrcLocation();
            Location dstLocation = record.getDstLocation();
            if (dstLocation == null)
                throw new IOException("Failed to determine destination location for " + srcLocation.getLocationUri());
            _wipe = !srcLocation.isEncrypted() && dstLocation.isEncrypted();
            if (tryMove(record))
                return true;
            copyFiles(record);
            Path srcPath = srcLocation.getCurrentPath();
            if (srcPath.isDirectory())
                _foldersToDelete.add(0, srcPath.getDirectory());
        }
        catch (NoFreeSpaceLeftException e)
        {
            throw new com.igeltech.nevercrypt.android.errors.NoFreeSpaceLeftException(_context);
        }
        catch (IOException e)
        {
            setError(e);
        }
        return true;
    }

    private boolean tryMove(SrcDst srcDst) throws IOException
    {
        Location srcLocation = srcDst.getSrcLocation();
        Path srcPath = srcLocation.getCurrentPath();
        Location dstLocation = srcDst.getDstLocation();
        if (dstLocation == null)
            throw new IOException("Failed to determine destination location for " + srcLocation.getLocationUri());
        Path dstPath = dstLocation.getCurrentPath();
        if (srcPath.getFileSystem() == dstPath.getFileSystem())
        {
            if (srcPath.isFile())
            {
                if (tryMove(srcPath.getFile(), dstPath.getDirectory()))
                {
                    ExtendedFileInfoLoader.getInstance().discardCache(srcLocation, srcPath);
                    return true;
                }
            }
            else if (srcPath.isDirectory())
                return tryMove(srcPath.getDirectory(), dstPath.getDirectory());
        }
        return false;
    }

    private boolean tryMove(FSRecord srcFile, Directory newParent) throws IOException
    {
        try
        {
            Path dstRec = calcDstPath(srcFile, newParent);
            if (dstRec != null)
            {
                if (dstRec.exists())
                    return false;
            }
            srcFile.moveTo(newParent);
            return true;
        }
        catch (UnsupportedOperationException e)
        {
            return false;
        }
    }

    @Override
    protected boolean copyFile(SrcDst record) throws IOException
    {
        if (super.copyFile(record))
        {
            Location srcLoc = record.getSrcLocation();
            ExtendedFileInfoLoader.getInstance().discardCache(srcLoc, srcLoc.getCurrentPath());
            return true;
        }
        return false;
    }

    @Override
    protected boolean copyFile(File srcFile, File dstFile) throws IOException
    {
        if (super.copyFile(srcFile, dstFile))
        {
            deleteFile(srcFile);
            return true;
        }
        return false;
    }

    private void deleteFile(File file) throws IOException
    {
        com.igeltech.nevercrypt.android.helpers.WipeFilesTask.wipeFile(file, _wipe, new com.igeltech.nevercrypt.android.helpers.WipeFilesTask.ITask()
        {
            @Override
            public void progress(int sizeInc)
            {
                //incProcessedSize(sizeInc);
            }

            @Override
            public boolean cancel()
            {
                return isCancelled();
            }
        });
    }

    private boolean deleteEmptyDir(Directory startDir) throws IOException
    {
        try (Directory.Contents dc = startDir.list())
        {
            if (dc.iterator().hasNext())
                return false;
        }
        startDir.delete();
        return true;
    }
}