package com.igeltech.nevercrypt.android.service;

import android.content.Context;
import android.content.Intent;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.helpers.TempFilesMonitor;
import com.igeltech.nevercrypt.fs.File;
import com.igeltech.nevercrypt.fs.util.FilesCountAndSize;
import com.igeltech.nevercrypt.fs.util.FilesOperationStatus;
import com.igeltech.nevercrypt.fs.util.SrcDstCollection;

import java.io.IOException;
import java.util.concurrent.CancellationException;

class WipeFilesTask extends DeleteFilesTask
{
    WipeFilesTask(boolean wipe)
    {
        _wipe = wipe;
    }

    private final boolean _wipe;
    private TempFilesMonitor _mon;

    @Override
    public Object doWork(Context context, Intent i) throws Throwable
    {
        _mon = TempFilesMonitor.getMonitor(context);
        return super.doWork(context, i);
    }

    @Override
    public void onCompleted(Result result)
    {
        try
        {
            result.getResult();
        }
        catch (CancellationException ignored)
        {

        }
        catch (Throwable e)
        {
            reportError(e);
        }
        finally
        {
            super.onCompleted(result);
        }
    }

    @Override
    protected int getNotificationMainTextId()
    {
        return R.string.wiping_files;
    }

    @Override
    protected FilesOperationStatus initStatus(SrcDstCollection records)
    {
        FilesOperationStatus status = new FilesOperationStatus();
        status.total = _wipe ? FilesCountAndSize.getFilesCountAndSize(false, records) : FilesCountAndSize.getFilesCount(records);
        return status;
    }

    @Override
    protected void deleteFile(File file) throws IOException
    {
        synchronized (_mon.getSyncObject())
        {
            com.igeltech.nevercrypt.android.helpers.WipeFilesTask.wipeFile(file, _wipe, new com.igeltech.nevercrypt.android.helpers.WipeFilesTask.ITask()
            {
                @Override
                public void progress(int sizeInc)
                {
                    incProcessedSize(sizeInc);
                }

                @Override
                public boolean cancel()
                {
                    return isCancelled();
                }
            });
        }
    }
}