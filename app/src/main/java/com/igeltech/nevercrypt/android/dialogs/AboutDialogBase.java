package com.igeltech.nevercrypt.android.dialogs;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.providers.MainContentProvider;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.util.StringPathUtil;
import com.igeltech.nevercrypt.locations.DeviceBasedLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.settings.GlobalConfig;
import com.igeltech.nevercrypt.util.exec.ExecuteExternalProgram;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class AboutDialogBase extends DialogFragment
{
    public static void showDialog(FragmentManager fm)
    {
        DialogFragment newFragment = new AboutDialog();
        newFragment.show(fm, "AboutDialog");
    }

    public static String getVersionName(Context context)
    {
        try
        {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (NameNotFoundException e)
        {
            Logger.log(e);
            return "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.about_dialog, container);
        String verName = getVersionName(getActivity());
        String aboutMessage = String.format("%s v%s\n%s", getResources().getString(R.string.app_name), verName, getResources().getString(R.string.about_message));
        ((AppCompatTextView) v.findViewById(R.id.about_text_view)).setText(aboutMessage);

        v.findViewById(R.id.homepage_button).setOnClickListener(v12 -> {
            try
            {
                openWebPage(GlobalConfig.HOMEPAGE_URL);
            }
            catch (Throwable e)
            {
                Logger.showAndLog(getActivity(), e);
            }
        });
        v.findViewById(R.id.get_program_log).setOnClickListener(view -> {
            try
            {
                saveDebugLog();
            }
            catch (Throwable e)
            {
                Logger.showAndLog(getActivity(), e);
            }
        });
        v.findViewById(R.id.close_button).setOnClickListener(v1 -> getDialog().cancel());
        return v;
    }

    protected void openWebPage(String url)
    {
        try
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        catch (Exception e)
        {
            Logger.showAndLog(getActivity(), e);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setWidthHeight();
    }

    protected void setWidthHeight()
    {
        Window w = getDialog().getWindow();
        if (w != null)
            w.setLayout(calcWidth(), calcHeight());
    }

    protected int calcWidth()
    {
        return getResources().getDimensionPixelSize(R.dimen.about_dialog_width);
    }

    protected int calcHeight()
    {
        return WRAP_CONTENT;
        //return getResources().getDimensionPixelSize(R.dimen.about_dialog_heigh);
    }

    private void saveDebugLog() throws IOException, ApplicationException
    {
        Context ctx = getActivity();
        File out = ctx.getExternalFilesDir(null);
        if (out == null || !out.canWrite())
            out = ctx.getFilesDir();
        Location loc = new DeviceBasedLocation(UserSettings.getSettings(ctx), new StringPathUtil(out.getPath()).combine(String.format(Locale.US, "log-%1$tY%1$tm%1$td%1$tH%1$tM%1$tS.txt", new Date())).toString());
        dumpLog(loc);
        sendLogFile(loc);
    }

    protected void dumpLog(Location logLocation) throws IOException, ApplicationException
    {
        ExecuteExternalProgram.executeAndReadString("logcat", "-df", logLocation.getCurrentPath().getPathString());
    }

    private void sendLogFile(Location logLocation)
    {
        Context ctx = getActivity();
        Uri uri = MainContentProvider.getContentUriFromLocation(logLocation);
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("text/plain");
        actionIntent.putExtra(Intent.EXTRA_STREAM, uri);

        ClipData cp = ClipData.newUri(ctx.getContentResolver(), ctx.getString(R.string.get_program_log), uri);
        actionIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        actionIntent.setClipData(cp);

        Intent startIntent = Intent.createChooser(actionIntent, ctx.getString(R.string.save_log_file_to));
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(startIntent);
    }
}
