package com.igeltech.nevercrypt.android.helpers;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.dialogs.MasterPasswordDialog;
import com.igeltech.nevercrypt.android.errors.UserException;
import com.igeltech.nevercrypt.android.filemanager.fragments.ExtStorageWritePermisisonCheckFragment;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.concurrent.CancellationException;

import io.reactivex.CompletableEmitter;

public class AppInitHelper extends AppInitHelperBase
{
    AppInitHelper(RxAppCompatActivity activity, CompletableEmitter emitter)
    {
        super(activity, emitter);
    }

    void startInitSequence()
    {
        MasterPasswordDialog.getObservable(_activity).
                flatMapCompletable(isValidPassword -> {
                    if (isValidPassword)
                        return ExtStorageWritePermisisonCheckFragment.getObservable(_activity);
                    throw new UserException(_activity, R.string.invalid_master_password);
                }).

                compose(_activity.bindToLifecycle()).
                subscribe(() -> {
                    convertLegacySettings();
                    _initFinished.onComplete();
                }, err -> {
                    if (!(err instanceof CancellationException))
                        Logger.log(err);
                });
    }
}
