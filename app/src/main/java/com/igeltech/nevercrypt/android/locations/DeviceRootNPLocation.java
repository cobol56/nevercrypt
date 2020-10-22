package com.igeltech.nevercrypt.android.locations;

import android.content.Context;
import android.net.Uri;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.locations.DeviceBasedLocation;
import com.igeltech.nevercrypt.settings.Settings;

public class DeviceRootNPLocation extends DeviceBasedLocation
{
	public static String getLocationId()
	{
		return URI_SCHEME;
	}

	public static final String URI_SCHEME = "rootfsnp";

	public DeviceRootNPLocation(Context context)
	{
		this(context,"");		
	}

	public DeviceRootNPLocation(Context context, String currentPath)
	{
		super(UserSettings.getSettings(context), context.getString(R.string.device),null,currentPath);
		_context = context;
	}
	
	public DeviceRootNPLocation(Context context, Settings settings, Uri locationUri)
	{
		super(settings, locationUri);
		_context = context;
	}

	@Override
	public Uri getLocationUri()
	{
		return makeFullUri().build();
	}

	@Override
	public Uri.Builder makeFullUri()
	{
		return super.makeFullUri().scheme(URI_SCHEME);
	}
	
	@Override
	public String getId()
	{
		return getLocationId();
	}

	@Override
	public DeviceRootNPLocation copy()
	{
		return new DeviceRootNPLocation(_context, _currentPathString);
	}

	private final Context _context;

}
