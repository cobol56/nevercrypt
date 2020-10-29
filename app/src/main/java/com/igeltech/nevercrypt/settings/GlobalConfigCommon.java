package com.igeltech.nevercrypt.settings;

import com.igeltech.nevercrypt.android.BuildConfig;

class GlobalConfigCommon
{
    public static final int FB_PREVIEW_WIDTH = 40;
    public static final int FB_PREVIEW_HEIGHT = 40;
    public static final int CLEAR_MASTER_PASS_INACTIVITY_TIMEOUT = 20 * 60 * 1000;
    public static final String HELP_URL = "https://www.igel-tech.com";

    public static boolean isDebug()
    {
        return BuildConfig.DEBUG;
    }
}
