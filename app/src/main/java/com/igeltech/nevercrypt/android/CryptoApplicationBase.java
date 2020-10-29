package com.igeltech.nevercrypt.android;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.igeltech.nevercrypt.android.helpers.ExtendedFileInfoLoader;
import com.igeltech.nevercrypt.android.providers.MainContentProvider;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.settings.SystemConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.igeltech.nevercrypt.android.settings.UserSettings.getSettings;

public class CryptoApplicationBase extends Application
{
    public static final String BROADCAST_EXIT = "com.igeltech.nevercrypt.android.BROADCAST_EXIT";

    public static void stopProgramBase(Context context, boolean removeNotifications)
    {
        LocalBroadcastManager.getInstance(context).sendBroadcastSync(new Intent(BROADCAST_EXIT));
        if (removeNotifications)
            NotificationManagerCompat.from(context).cancelAll();
        setMasterPassword(null);
        LocationsManager.setGlobalLocationsManager(null);
        UserSettings.closeSettings();
        try
        {
            ExtendedFileInfoLoader.closeInstance();
        }
        catch (Throwable e)
        {
            Logger.log(e);
        }

        try
        {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            if (MainContentProvider.hasSelectionInClipboard(cm))
                cm.setPrimaryClip(ClipData.newPlainText("Empty", ""));
        }
        catch (Throwable e)
        {
            Logger.log(e);
        }
    }

    public static void exitProcess()
    {
        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    System.exit(0);
                }
                catch (Throwable e)
                {
                    Logger.log(e);
                }
            }
        }, 4000);
    }

    public void onCreate()
    {
        super.onCreate();

        SystemConfig.setInstance(new com.igeltech.nevercrypt.android.settings.SystemConfig(getApplicationContext()));

        UserSettings us;
        try
        {
            us = getSettings(getApplicationContext());
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            Toast.makeText(this, Logger.getExceptionMessage(this, e), Toast.LENGTH_LONG).show();
            return;
        }
        init(us);
        Logger.debug("Android sdk version is " + Build.VERSION.SDK_INT);
    }

    public synchronized static SecureBuffer getMasterPassword()
    {
        return _masterPass;
    }

    public synchronized static void setMasterPassword(SecureBuffer pass)
    {
        if (_masterPass != null)
        {
            _masterPass.close();
            _masterPass = null;
        }
        _masterPass = pass;
    }

    public synchronized static void clearMasterPassword()
    {
        if (_masterPass != null)
        {
            _masterPass.close();
            _masterPass = null;
        }
    }

    public static synchronized Map<String, String> getMimeTypesMap(Context context)
    {
        if (_mimeTypes == null)
        {
            try
            {
                _mimeTypes = loadMimeTypes(context);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Failed loading mime types database", e);
            }
        }
        return _mimeTypes;
    }

    public static synchronized long getLastActivityTime()
    {
        return _lastActivityTime;
    }

    public static synchronized void updateLastActivityTime()
    {
        _lastActivityTime = SystemClock.elapsedRealtime();
    }

    protected void init(UserSettings settings)
    {
        try
        {
            if (settings.disableDebugLog())
                Logger.disableLog(true);
            else
                Logger.initLogger();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            Toast.makeText(this, Logger.getExceptionMessage(this, e), Toast.LENGTH_LONG).show();
        }
    }

    private static SecureBuffer _masterPass;
    private static Map<String, String> _mimeTypes;
    private static long _lastActivityTime;
    private static final String MIME_TYPES_PATH = "mime.types";

    private static Map<String, String> loadMimeTypes(Context context) throws IOException
    {
        Pattern p = Pattern.compile("^([^\\s/]+/[^\\s/]+)\\s+(.+)$");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(MIME_TYPES_PATH))))
        {
            HashMap<String, String> map = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null)
            {
                Matcher m = p.matcher(line);
                if (m.matches())
                {
                    String mimeType = m.group(1);
                    String extsString = m.group(2);
                    String[] exts = extsString.split("\\s");
                    for (String s : exts)
                        map.put(s, mimeType);
                }
            }
            return map;
        }
    }
}
