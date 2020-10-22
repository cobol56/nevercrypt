package com.igeltech.nevercrypt.android.settings.program;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.errors.UserException;
import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.helpers.ProgressDialogTaskFragmentCallbacks;
import com.igeltech.nevercrypt.android.settings.ButtonPropertyEditor;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.fs.exfat.ExFat;
import com.igeltech.nevercrypt.fs.std.StdFs;
import com.igeltech.nevercrypt.fs.util.PathUtil;
import com.igeltech.nevercrypt.fs.util.Util;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.settings.GlobalConfig;

import java.io.File;
import java.io.IOException;

import static com.igeltech.nevercrypt.locations.LocationsManagerBase.PARAM_LOCATION_URI;

public class InstallExFatModulePropertyEditor extends ButtonPropertyEditor
{

    public static class InstallExfatModuleTask extends TaskFragment
    {
        public static final String TAG = "InstallExfatModuleTask";

        public static InstallExfatModuleTask newInstance(Uri moduleLocationUri)
        {
            Bundle args = new Bundle();
            args.putParcelable(PARAM_LOCATION_URI, moduleLocationUri);
            InstallExfatModuleTask f = new InstallExfatModuleTask();
            f.setArguments(args);
            return f;
        }

        @Override
        protected void initTask(FragmentActivity activity)
        {
            _context = activity.getApplicationContext();
        }

        @Override
        protected void doWork(TaskState uiUpdater) throws Exception
        {

            Location moduleLocation = LocationsManager.getLocationsManager(_context).getFromBundle(getArguments(), null);
            if(!moduleLocation.getCurrentPath().isFile())
                throw new UserException(_context, R.string.file_not_found);

            File targetPath = ExFat.getModulePath();
            Path targetFolderPath = StdFs.getStdFs().getPath(
                    targetPath.getParent()
            );
            PathUtil.makeFullPath(targetFolderPath);
            Util.copyFile(
                    moduleLocation.getCurrentPath().getFile(),
                    targetFolderPath.getDirectory(),
                    targetPath.getName());
            if(!ExFat.isModuleInstalled() && !ExFat.isModuleIncompatible())
            {
                ExFat.loadNativeLibrary();
                uiUpdater.setResult(true);
            }
            else
                uiUpdater.setResult(false);

        }

        @Override
        protected TaskCallbacks getTaskCallbacks(final FragmentActivity activity)
        {
            return new ProgressDialogTaskFragmentCallbacks(activity, R.string.loading)
            {
                @Override
                public void onCompleted(Bundle args, Result result)
                {
                    try
                    {
                        if((Boolean)result.getResult())
                            Toast.makeText(activity, R.string.module_has_been_installed, Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(activity, R.string.restart_application, Toast.LENGTH_LONG).show();
                    }
                    catch (Throwable e)
                    {
                        Logger.showAndLog(_context, e);
                    }
                }
            };
        }

        private Context _context;
    }

    public InstallExFatModulePropertyEditor(Host host)
    {
        super(host,
                R.string.install_exfat_module,
                host.getContext().getString(R.string.install_exfat_module),
                host.getContext().getString(R.string.install_exfat_module_desc, GlobalConfig.EXFAT_MODULE_URL),
                host.getContext().getString(R.string.select_file));
    }

    @Override
    protected void onButtonClick()
    {
        try
        {
            requestActivity(getSelectPathIntent(), SELECT_PATH_REQ_CODE);
        }
        catch(Exception e)
        {
            Logger.showAndLog(getHost().getContext(), e);
        }
    }

    @Override
    protected void onPropertyRequestResult(int propertyRequestCode, int resultCode, Intent data)
    {
        if(propertyRequestCode == SELECT_PATH_REQ_CODE && resultCode == AppCompatActivity.RESULT_OK && data!=null)
            onPathSelected(data);
    }

    private Intent getSelectPathIntent() throws IOException
    {
        return FileManagerActivity.getSelectPathIntent(
                getHost().getContext(),
                null,
                false,
                true,
                false,
                false,
                true,
                true);
    }

    private void onPathSelected(Intent result)
    {
        Uri uri = result.getData();
        getHost().
                getFragmentManager().
                beginTransaction().
                add(InstallExfatModuleTask.newInstance(uri), InstallExfatModuleTask.TAG)
                .commit();
    }

    private static final int SELECT_PATH_REQ_CODE = 1;
}
