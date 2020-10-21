package com.sovworks.eds.android.helpers;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import com.sovworks.eds.android.Logger;
import com.sovworks.eds.crypto.SecureBuffer;
import com.sovworks.eds.crypto.SimpleCrypto;
import com.sovworks.eds.locations.LocationsManager;
import com.sovworks.eds.locations.Openable;

import java.io.IOException;

public class Util extends UtilBase
{
    public static SecureBuffer getPassword(Bundle args, @SuppressWarnings("UnusedParameters") LocationsManager lm) throws IOException
    {
        return args.getParcelable(Openable.PARAM_PASSWORD);
    }


    public static String getDefaultSettingsPassword(Context context)
    {
        try
        {
            return SimpleCrypto.calcStringMD5(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        }
        catch(Exception e)
        {
            Logger.log(e);
        }
        return "";
    }
}

