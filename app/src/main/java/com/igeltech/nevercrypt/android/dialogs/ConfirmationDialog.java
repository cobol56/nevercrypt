package com.igeltech.nevercrypt.android.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.igeltech.nevercrypt.android.R;

public abstract class ConfirmationDialog extends AppCompatDialogFragment
{
    public static final String ARG_RECEIVER_TAG = "com.igeltech.nevercrypt.android.RECEIVER_TAG";

    public interface Receiver
    {
        void onYes();

        void onNo();
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getTitle()).setCancelable(false).setPositiveButton(R.string.yes, (dialog, id) -> {
            onYes();
            dismiss();
        }).setNegativeButton(R.string.no, (dialog, id) -> {
            onNo();
            dismiss();
        });

        return builder.create();
    }

    protected void onNo()
    {
        Receiver rec = getReceiver();
        if (rec != null)
            rec.onNo();
    }

    protected void onYes()
    {
        Receiver rec = getReceiver();
        if (rec != null)
            rec.onYes();
    }

    protected abstract String getTitle();

    protected Receiver getReceiver()
    {
        Bundle args = getArguments();
        String tag = args == null ? null : args.getString(ARG_RECEIVER_TAG);
        if (tag != null)
        {
            Fragment f = getFragmentManager().findFragmentByTag(tag);
            if (f instanceof Receiver)
                return (Receiver) f;
        }
        else
        {
            FragmentActivity act = getActivity();
            if (act instanceof Receiver)
                return (Receiver) act;
        }
        return null;
    }
}
