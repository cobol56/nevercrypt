package com.igeltech.nevercrypt.android.helpers;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.crypto.SimpleCrypto;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.locations.Openable;

import java.io.IOException;

public class Util extends UtilBase
{
    public static SecureBuffer getPassword(Bundle args, LocationsManager lm) throws IOException
    {
        return args.getParcelable(Openable.PARAM_PASSWORD);
    }

    public static String getDefaultSettingsPassword(Context context)
    {
        try
        {
            return SimpleCrypto.calcStringMD5(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        }
        catch (Exception e)
        {
            Logger.log(e);
        }
        return "";
    }
}

