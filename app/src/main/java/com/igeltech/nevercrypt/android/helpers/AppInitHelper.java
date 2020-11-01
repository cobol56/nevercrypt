package com.igeltech.nevercrypt.android.helpers;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.dialogs.MasterPasswordDialog;
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
                compose(_activity.bindToLifecycle()).
                subscribe((isValidPassword) -> {
                    if (isValidPassword)
                    {
                        convertLegacySettings();
                        _initFinished.onComplete();
                    }
                    else
                        _activity.finish();
                }, err -> {
                    if (!(err instanceof CancellationException))
                        Logger.log(err);
                });
    }
}
