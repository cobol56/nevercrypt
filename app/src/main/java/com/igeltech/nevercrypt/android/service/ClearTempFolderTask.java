package com.igeltech.nevercrypt.android.service;

import android.content.Context;
import android.content.Intent;

import com.igeltech.nevercrypt.android.CryptoApplication;
import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.fs.util.SrcDstCollection;
import com.igeltech.nevercrypt.fs.util.SrcDstPlain;
import com.igeltech.nevercrypt.fs.util.SrcDstRec;
import com.igeltech.nevercrypt.fs.util.SrcDstSingle;
import com.igeltech.nevercrypt.locations.Location;

import java.io.IOException;
import java.util.concurrent.CancellationException;

public class ClearTempFolderTask extends WipeFilesTask
{
    public static final String ARG_EXIT_PROGRAM = "com.igeltech.nevercrypt.android.EXIT_PROGRAM";

    public ClearTempFolderTask()
    {
        super(true);
    }

    public static SrcDstCollection getMirrorFiles(Context context) throws IOException
    {
        Location loc = FileOpsService.getSecTempFolderLocation(UserSettings.getSettings(context).getWorkDir(), context);
        if (loc.getCurrentPath() != null && loc.getCurrentPath().exists())
        {
            SrcDstRec sdr = new SrcDstRec(new SrcDstSingle(FileOpsService.getSecTempFolderLocation(UserSettings.getSettings(context).getWorkDir(), context), null));
            sdr.setIsDirLast(true);
            return sdr;
        }
        else
            return new SrcDstPlain();
    }

    @Override
    protected Param getParam()
    {
        return (Param) super.getParam();
    }

    @Override
    protected FileOperationParam initParam(Intent i)
    {
        return new Param(i, _context);
    }

    @Override
    public void onCompleted(Result result)
    {
        if (getParam().shouldExitProgram())
        {
            try
            {
                removeNotification();
                result.getResult();
                CryptoApplication.stopProgram(_context, true);
            }
            catch (CancellationException ignored)
            {
            }
            catch (Throwable e)
            {
                reportError(e);
                CryptoApplication.stopProgram(_context, false);
            }
        }
        else
            super.onCompleted(result);
    }

    public static class Param extends FileOperationTaskBase.FileOperationParam
    {
        private final Context _context;

        public Param(Intent i, Context context)
        {
            super(i);
            _context = context;
        }

        public boolean shouldExitProgram()
        {
            return getIntent().getBooleanExtra(ARG_EXIT_PROGRAM, false);
        }

        @Override
        protected SrcDstCollection loadRecords(Intent i)
        {
            try
            {
                return getMirrorFiles(_context);
            }
            catch (IOException e)
            {
                Logger.log(e);
            }
            return null;
        }
    }
}