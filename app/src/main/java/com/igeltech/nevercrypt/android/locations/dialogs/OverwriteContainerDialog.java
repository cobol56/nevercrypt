package com.igeltech.nevercrypt.android.locations.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.activities.SettingsBaseActivity;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;

public class OverwriteContainerDialog extends DialogFragment
{
    private static final String ARG_REQUEST_RES_ID = "com.igeltech.nevercrypt.android.TEXT_ID";

    public static void showDialog(FragmentManager fm)
    {
        showDialog(fm, 0);
    }

    public static void showDialog(FragmentManager fm, int requestResId)
    {
        DialogFragment newFragment = new OverwriteContainerDialog();
        if (requestResId > 0)
        {
            Bundle args = new Bundle();
            args.putInt(ARG_REQUEST_RES_ID, requestResId);
            newFragment.setArguments(args);
        }
        newFragment.show(fm, "com.igeltech.nevercrypt.android.locations.dialogs.OverwriteContainerDialog");
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        int resId = args != null ? args.getInt(ARG_REQUEST_RES_ID, R.string.do_you_want_to_overwrite_existing_file) : R.string.do_you_want_to_overwrite_existing_file;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(resId).setPositiveButton(R.string.yes, (dialog, id) -> {
            dialog.dismiss();
            doOverwrite();
        }).setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    protected void doOverwrite()
    {
        CreateLocationFragment f = (CreateLocationFragment) getFragmentManager().findFragmentByTag(SettingsBaseActivity.SETTINGS_FRAGMENT_TAG);
        if (f != null)
        {
            f.setOverwrite(true);
            f.startCreateLocationTask();
        }
    }
}
