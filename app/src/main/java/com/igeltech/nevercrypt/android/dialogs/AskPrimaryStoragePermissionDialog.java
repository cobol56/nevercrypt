package com.igeltech.nevercrypt.android.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.fragments.ExtStorageWritePermisisonCheckFragment;
import com.trello.rxlifecycle3.components.support.RxAppCompatDialogFragment;

public class AskPrimaryStoragePermissionDialog extends RxAppCompatDialogFragment
{
    public static void showDialog(FragmentManager fm)
    {
        RxAppCompatDialogFragment newFragment = new AskPrimaryStoragePermissionDialog();
        newFragment.show(fm, "AskPrimaryStoragePermissionDialog");
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.storage_permission_desc).setPositiveButton(R.string.grant, (dialog, id) -> {
            dialog.dismiss();
            ExtStorageWritePermisisonCheckFragment stateFragment = (ExtStorageWritePermisisonCheckFragment) getFragmentManager().findFragmentByTag(ExtStorageWritePermisisonCheckFragment.TAG);
            if (stateFragment != null)
                stateFragment.requestExtStoragePermission();
        }).setNegativeButton(android.R.string.cancel, (dialog, id) -> {
            ExtStorageWritePermisisonCheckFragment stateFragment = (ExtStorageWritePermisisonCheckFragment) getFragmentManager().findFragmentByTag(ExtStorageWritePermisisonCheckFragment.TAG);
            if (stateFragment != null)
                stateFragment.cancelExtStoragePermissionRequest();
        });
        return builder.create();
    }
}
