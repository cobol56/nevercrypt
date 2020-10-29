package com.igeltech.nevercrypt.android.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.CryptoApplication;
import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.settings.GlobalConfig;
import com.igeltech.nevercrypt.settings.Settings;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.SETTINGS_PROTECTION_KEY_CHECK;

public class MasterPasswordDialog extends PasswordDialog
{
    public static final String TAG = "com.igeltech.nevercrypt.android.dialogs.MasterPasswordDialog";
    public static final String ARG_IS_OBSERVABLE = "com.igeltech.nevercrypt.android.IS_OBSERVABLE";

    public static Single<Boolean> getObservable(RxAppCompatActivity activity)
    {
        UserSettings s = UserSettings.getSettings(activity);
        long curTime = SystemClock.elapsedRealtime();
        long lastActTime = CryptoApplication.getLastActivityTime();
        if (curTime - lastActTime > GlobalConfig.CLEAR_MASTER_PASS_INACTIVITY_TIMEOUT)
        {
            Logger.debug("Clearing settings protection key");
            CryptoApplication.clearMasterPassword();
            s.clearSettingsProtectionKey();
        }
        CryptoApplication.updateLastActivityTime();
        try
        {
            s.getSettingsProtectionKey();
        }
        catch (Settings.InvalidSettingsPassword e)
        {
            FragmentManager fm = activity.getSupportFragmentManager();
            MasterPasswordDialog mpd = (MasterPasswordDialog) fm.findFragmentByTag(TAG);
            if (mpd == null)
            {
                MasterPasswordDialog masterPasswordDialog = new MasterPasswordDialog();
                Bundle args = new Bundle();
                args.putBoolean(ARG_IS_OBSERVABLE, true);
                masterPasswordDialog.setArguments(args);
                return masterPasswordDialog.
                        _passwordCheckSubject.
                        doOnSubscribe(subscription -> masterPasswordDialog.show(fm, TAG)).
                        firstOrError();
            }
            return mpd.
                    _passwordCheckSubject.
                    firstOrError();
        }
        return Single.just(true);
    }

    public static boolean checkSettingsKey(Context context)
    {
        UserSettings settings = UserSettings.getSettings(context);
        try
        {
            String check = settings.getProtectedString(SETTINGS_PROTECTION_KEY_CHECK);
            if (check == null)
                settings.saveSettingsProtectionKey();
            CryptoApplication.updateLastActivityTime();
            return true;
        }
        catch (Settings.InvalidSettingsPassword ignored)
        {
            settings.clearSettingsProtectionKey();
            Toast.makeText(context, R.string.invalid_master_password, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public static boolean checkMasterPasswordIsSet(Context context, FragmentManager fm, String receiverFragmentTag)
    {
        UserSettings s = UserSettings.getSettings(context);
        long curTime = SystemClock.elapsedRealtime();
        long lastActTime = CryptoApplication.getLastActivityTime();
        if (curTime - lastActTime > GlobalConfig.CLEAR_MASTER_PASS_INACTIVITY_TIMEOUT)
        {
            Logger.debug("Clearing settings protection key");
            CryptoApplication.clearMasterPassword();
            s.clearSettingsProtectionKey();
        }
        CryptoApplication.updateLastActivityTime();
        try
        {
            s.getSettingsProtectionKey();
        }
        catch (Settings.InvalidSettingsPassword e)
        {
            MasterPasswordDialog mpd = new MasterPasswordDialog();
            if (receiverFragmentTag != null)
            {
                Bundle args = new Bundle();
                args.putString(ARG_RECEIVER_FRAGMENT_TAG, receiverFragmentTag);
                mpd.setArguments(args);
            }
            mpd.show(fm, TAG);
            return false;
        }
        return true;
    }

    @Override
    protected String loadLabel()
    {
        return getString(R.string.enter_master_password);
    }

    @Override
    public boolean hasPassword()
    {
        return true;
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        CryptoApplication.clearMasterPassword();
        super.onCancel(dialog);
    }

    @Override
    protected void onPasswordEntered()
    {
        CryptoApplication.setMasterPassword(new SecureBuffer(getPassword()));
        if (checkSettingsKey(getActivity()))
        {
            Bundle args = getArguments();
            if (args != null && args.getBoolean(ARG_IS_OBSERVABLE))
                _passwordCheckSubject.onNext(true);
            else
                super.onPasswordEntered();
        }
        else
            onPasswordNotEntered();
    }

    @Override
    protected void onPasswordNotEntered()
    {
        Bundle args = getArguments();
        if (args != null && args.getBoolean(ARG_IS_OBSERVABLE))
            _passwordCheckSubject.onNext(false);
        else
            super.onPasswordNotEntered();
    }

    private final Subject<Boolean> _passwordCheckSubject = BehaviorSubject.create();
}
