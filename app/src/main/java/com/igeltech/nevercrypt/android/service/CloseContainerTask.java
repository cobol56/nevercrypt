package com.igeltech.nevercrypt.android.service;

import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.closer.fragments.OMLocationCloserFragment;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.locations.EDSLocation;
import com.igeltech.nevercrypt.locations.LocationsManager;

public class CloseContainerTask extends ServiceTaskWithNotificationBase
{

	@Override
	public Object doWork(Context context, Intent i) throws Throwable
	{
		super.doWork(context, i);
		EDSLocation cont = (EDSLocation) LocationsManager.getLocationsManager(context).getFromIntent(i, null);
		if(cont!=null)
			OMLocationCloserFragment.unmountAndClose(context, cont, UserSettings.getSettings(context).alwaysForceClose());
		return null;
	}

    @Override
    protected NotificationCompat.Builder initNotification()
    {
        return super.initNotification().setContentTitle(_context.getString(R.string.closing));
    }

}
