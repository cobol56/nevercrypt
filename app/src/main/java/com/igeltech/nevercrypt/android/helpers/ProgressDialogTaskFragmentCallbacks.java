package com.igeltech.nevercrypt.android.helpers;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.igeltech.nevercrypt.android.fragments.TaskFragment.Result;
import com.igeltech.nevercrypt.android.fragments.TaskFragment.TaskCallbacks;

public class ProgressDialogTaskFragmentCallbacks implements TaskCallbacks
{
    protected final Activity _host;
    private final int _dialogTextResId;
    private DialogFragment _dialog;

    public ProgressDialogTaskFragmentCallbacks(Activity host, int dialogTextResId)
    {
        _host = host;
        _dialogTextResId = dialogTextResId;
    }

    @Override
    public void onPrepare(Bundle args)
    {
    }

    @Override
    public void onResumeUI(Bundle args)
    {
        _dialog = initDialog(args);
        if (_dialog != null)
            _dialog.show(_host.getFragmentManager(), Dialog.TAG);
    }

    @Override
    public void onSuspendUI(Bundle args)
    {
        if (_dialog != null)
            _dialog.dismissAllowingStateLoss();
    }

    @Override
    public void onUpdateUI(Object state)
    {
    }

    @Override
    public void onCompleted(Bundle args, Result result)
    {
    }

    protected DialogFragment initDialog(Bundle args)
    {
        return Dialog.newInstance(_host.getText(_dialogTextResId).toString());
    }

    public static class Dialog extends DialogFragment
    {
        public static final String TAG = "ProgressDialog";
        public static final String ARG_DIALOG_TEXT = "dialog_text";

        public static Dialog newInstance(String dialogText)
        {
            Bundle args = new Bundle();
            args.putString(ARG_DIALOG_TEXT, dialogText);
            Dialog d = new Dialog();
            d.setArguments(args);
            return d;
        }

        @NonNull
        @Override
        public android.app.Dialog onCreateDialog(Bundle savedInstanceState)
        {
            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getArguments().getString(ARG_DIALOG_TEXT));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            return dialog;
        }
    }
}
