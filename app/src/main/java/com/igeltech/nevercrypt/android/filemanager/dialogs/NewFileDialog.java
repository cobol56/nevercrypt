package com.igeltech.nevercrypt.android.filemanager.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.tasks.CreateNewFile;

public class NewFileDialog extends DialogFragment
{
    private static final String ARG_TYPE = "com.igeltech.nevercrypt.android.TYPE";
    private static final String ARG_RECEIVER_TAG = "com.igeltech.nevercrypt.android.RECEIVER_TAG";

    public static void showDialog(FragmentManager fm, int type, String receiverTag)
    {
        DialogFragment newFragment = new NewFileDialog();
        Bundle b = new Bundle();
        b.putInt(ARG_TYPE, type);
        b.putString(ARG_RECEIVER_TAG, receiverTag);
        newFragment.setArguments(b);
        newFragment.show(fm, "NewFileDialog");
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState)
    {
        int ft = getArguments().getInt(ARG_TYPE);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //alert.setMessage(getString(R.string.enter_new_file_name));
        // Set an EditText view to get user input
        final AppCompatEditText input = new AppCompatEditText(getActivity());
        input.setSingleLine();
        input.setHint(getString(ft == CreateNewFile.FILE_TYPE_FOLDER ? R.string.enter_new_folder_name : R.string.enter_new_file_name));
        alert.setView(input);
        alert.setPositiveButton(getString(android.R.string.ok), (dialog, whichButton) -> {
            Receiver r = (Receiver) getFragmentManager().findFragmentByTag(getArguments().getString(ARG_RECEIVER_TAG));
            if (r != null)
                r.makeNewFile(input.getText().toString(), getArguments().getInt(ARG_TYPE));
            dialog.dismiss();
        });
        alert.setNegativeButton(android.R.string.cancel, (dialog, whichButton) -> {
            // Canceled.
        });
        return alert.create();
    }

    public interface Receiver
    {
        void makeNewFile(String name, int type);
    }
}
