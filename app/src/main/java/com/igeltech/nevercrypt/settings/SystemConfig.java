package com.igeltech.nevercrypt.settings;

public abstract class SystemConfig extends SystemConfigCommon
{
    private static SystemConfig _instance;

    public static SystemConfig getInstance()
    {
        return _instance;
    }

    public static void setInstance(SystemConfig systemConfig)
    {
        _instance = systemConfig;
    }
}
