package com.igeltech.nevercrypt.android.helpers;

import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.settings.Settings;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;

import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.CURRENT_SETTINGS_VERSION;

public abstract class AppInitHelperBase
{
    protected final UserSettings _settings;
    final RxAppCompatActivity _activity;
    final CompletableEmitter _initFinished;

    AppInitHelperBase(RxAppCompatActivity activity, CompletableEmitter emitter)
    {
        _activity = activity;
        _settings = UserSettings.getSettings(activity);
        _initFinished = emitter;
    }

    public static Completable createObservable(RxAppCompatActivity activity)
    {
        return Completable.create(emitter -> {
            AppInitHelper initHelper = new AppInitHelper(activity, emitter);
            initHelper.startInitSequence();
        });
    }

    void convertLegacySettings()
    {
        int curSettingsVersion = _settings.getCurrentSettingsVersion();
        if (curSettingsVersion >= Settings.VERSION)
            return;
        if (curSettingsVersion < 0)
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
