package com.igeltech.nevercrypt.android.locations.opener.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.dialogs.AskExtStorageWritePermissionDialog;
import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.locations.DocumentTreeLocation;
import com.igeltech.nevercrypt.android.locations.ExternalStorageLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.io.File;
import java.io.IOException;

import static com.igeltech.nevercrypt.android.locations.opener.fragments.LocationOpenerBaseFragment.OpenLocationTaskFragment.ARG_OPENER_TAG;

public class ExternalStorageOpenerFragment extends LocationOpenerBaseFragment
{
    public static class CheckLocationWritableTaskFragment extends TaskFragment
    {
        enum ResultType
        {
            OK,
            AskPermission,
            DontAskPermission
        }

        public static final String TAG = "CheckLocationWritableTaskFragment";

        @Override
        protected void initTask(FragmentActivity activity)
        {
            _lm = LocationsManager.getLocationsManager(activity);
        }

        @Override
        protected void doWork(TaskState state) throws Throwable
        {
            ExternalStorageLocation loc = getTargetLocation();
            DocumentTreeLocation docTreeLocation = getDocTreeLocation(_lm, loc);
            if(docTreeLocation != null && docTreeLocation.getFS().getRootPath().exists())
                state.setResult(ResultType.OK);
            else
                state.setResult(isWritable(loc) ? ResultType.DontAskPermission : ResultType.AskPermission);
        }

        private boolean isWritable(ExternalStorageLocation loc)
        {
            try
            {
                File res = File.createTempFile("crypto", null, new File(loc.getRootPath()));
                res.delete();
                return true;
            }
            catch (IOException ignored)
            {
            }
            return false;

        }
        @Override
        protected TaskCallbacks getTaskCallbacks(FragmentActivity activity)
        {
            final ExternalStorageOpenerFragment f = (ExternalStorageOpenerFragment) getFragmentManager().findFragmentByTag(getArguments().getString(ARG_OPENER_TAG));
            return f == null ? null : new TaskCallbacks()
            {
                @Override
                public void onPrepare(Bundle args)
                {

                }

                @Override
                public void onUpdateUI(Object state)
                {

                }

                @Override
                public void onResumeUI(Bundle args)
                {

                }

                @Override
                public void onSuspendUI(Bundle args)
                {

                }

                @Override
                public void onCompleted(Bundle args, Result result)
                {
                    try
                    {
                        if(result.getResult() == ResultType.OK)
                            f.openLocation();
                        else if(result.getResult() == ResultType.AskPermission)
                        {
                            f.askWritePermission();
                            return;
                        }
                    }
                    catch (Throwable e)
                    {
                        Logger.log(e);
                    }
                    f.setDontAskPermission();
                    f.openLocation();
                }
            };
        }

        private ExternalStorageLocation getTargetLocation() throws Exception
        {
            Uri locationUri = getArguments().getParcelable(LocationsManager.PARAM_LOCATION_URI);
            return (ExternalStorageLocation) _lm.getLocation(locationUri);
        }

        private LocationsManager _lm;
    }

    private void setDontAskPermission()
    {
        ExternalStorageLocation loc = getTargetLocation();
        loc.getExternalSettings().setDontAskWritePermission(true);
        loc.saveExternalSettings();
    }

    public void setDontAskPermissionAndOpenLocation()
    {
        setDontAskPermission();
        openLocation();
    }

    public void cancelOpen()
    {
        finishOpener(false, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showSystemDialog()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_ADD_LOCATION);
        Toast.makeText(getActivity(), R.string.select_root_folder_tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_CODE_ADD_LOCATION)
        {
            if(resultCode == AppCompatActivity.RESULT_OK)
            {
                Uri treeUri = data.getData();
                if(treeUri!=null)
                {
                    try
                    {
                        getActivity().getContentResolver().takePersistableUriPermission(treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    catch (SecurityException e)
                    {
                        Logger.log(e);
                    }

                    DocumentTreeLocation loc = new DocumentTreeLocation(getActivity().getApplicationContext(), treeUri);
                    loc.getExternalSettings().setVisibleToUser(false);
                    loc.saveExternalSettings();
                    LocationsManager.getLocationsManager(getActivity()).addNewLocation(loc, true);
                    ExternalStorageLocation tloc = getTargetLocation();
                    tloc.getExternalSettings().setDocumentsAPIUriString(loc.getLocationUri().toString());
                    tloc.saveExternalSettings();
                }
                openLocation();
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void openLocation()
    {
        ExternalStorageLocation loc = getTargetLocation();
        String docUri = loc.getExternalSettings().getDocumentsAPIUriString();
        if (docUri != null)
        {
            LocationsManager lm = LocationsManager.getLocationsManager(getActivity());
            try
            {
                Location docLoc = lm.getLocation(Uri.parse(docUri));
                finishOpener(true, docLoc);
                return;
            }
            catch (Exception e)
            {
                Logger.showAndLog(getActivity(), e);
            }
        } else if (!loc.getExternalSettings().dontAskWritePermission())
        {
            startCheckWritableTask(loc);
            return;
        }
        super.openLocation();
    }

    @Override
    protected ExternalStorageLocation getTargetLocation()
    {
        return (ExternalStorageLocation)super.getTargetLocation();
    }

    String getCheckWritableTaskTag(Location loc)
    {
        return CheckLocationWritableTaskFragment.TAG + loc.getId();
    }

    private static DocumentTreeLocation getDocTreeLocation(LocationsManager lm, ExternalStorageLocation extLoc)
    {
        String docUri = extLoc.getExternalSettings().getDocumentsAPIUriString();
        if (docUri != null)
        {
            try
            {
                return (DocumentTreeLocation) lm.getLocation(Uri.parse(docUri));
            }
            catch (Exception e)
            {
                Logger.log(e);
            }
        }
        return null;
    }

    private void startCheckWritableTask(Location loc)
    {
        Bundle args = new Bundle();
        LocationsManager.storePathsInBundle(args, loc, null );
        args.putString(ARG_OPENER_TAG, getTag());
        TaskFragment f = new CheckLocationWritableTaskFragment();
        f.setArguments(args);
        getFragmentManager().beginTransaction().add(f, getCheckWritableTaskTag(loc)).commit();
    }

    private void askWritePermission()
    {
        AskExtStorageWritePermissionDialog.showDialog(getFragmentManager(), getTag());
    }

    private static final int REQUEST_CODE_ADD_LOCATION = AppCompatActivity.RESULT_FIRST_USER;

}
