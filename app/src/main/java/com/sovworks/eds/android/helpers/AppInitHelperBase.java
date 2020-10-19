package com.sovworks.eds.android.helpers;

import android.annotation.SuppressLint;

import com.sovworks.eds.android.settings.UserSettings;
import com.sovworks.eds.settings.Settings;
import com.trello.rxlifecycle3.components.RxActivity;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;

import static com.sovworks.eds.android.settings.UserSettingsCommon.CURRENT_SETTINGS_VERSION;

public abstract class AppInitHelperBase
{
    public static Completable createObservable(RxActivity activity)
    {
        return Completable.create(emitter -> {
            AppInitHelper initHelper = new AppInitHelper(activity, emitter);
            initHelper.startInitSequence();
        });
    }

    AppInitHelperBase(RxActivity activity, CompletableEmitter emitter)
    {
        _activity = activity;
        _settings = UserSettings.getSettings(activity);
        _initFinished = emitter;
    }

    final RxActivity _activity;
    protected final UserSettings _settings;
    final CompletableEmitter _initFinished;

    @SuppressLint("ApplySharedPref")
    void convertLegacySettings()
    {
        int curSettingsVersion = _settings.getCurrentSettingsVersion();
        if(curSettingsVersion >= Settings.VERSION)
            return;

        if(curSettingsVersion < 0)
        {
            _settings.getSharedPreferences().edit().putInt(CURRENT_SETTINGS_VERSION, Settings.VERSION).commit();
        }

        _settings.
                getSharedPreferences().
                edit().
                putInt(UserSettings.CURRENT_SETTINGS_VERSION, Settings.VERSION).
                commit();
    }
}
