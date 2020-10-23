package com.igeltech.nevercrypt.android;

import android.content.Context;

public class CryptoApplication extends CryptoApplicationBase
{

    public static void stopProgram(Context context, boolean exitProcess)
    {
        stopProgramBase(context, exitProcess);
        if(exitProcess)
            exitProcess();
    }

}
