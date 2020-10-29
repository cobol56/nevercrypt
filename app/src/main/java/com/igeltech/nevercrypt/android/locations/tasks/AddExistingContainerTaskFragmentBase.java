package com.igeltech.nevercrypt.android.locations.tasks;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.errors.UserException;
import com.igeltech.nevercrypt.android.locations.EncFsLocation;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.container.ContainerFormatter;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.fs.encfs.Config;
import com.igeltech.nevercrypt.locations.ContainerLocation;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.settings.Settings;

import java.io.IOException;

public abstract class AddExistingContainerTaskFragmentBase extends AddExistingCryptoLocationTaskFragment
{
    public static final String TAG = "com.igeltech.nevercrypt.android.locations.tasks.AddExistingContainerTaskFragment";

    @Override
    protected CryptoLocation createCryptoLocation(Location locationLocation) throws Exception
    {
        Logger.debug("Adding location at " + locationLocation.getLocationUri());
        Path cp = locationLocation.getCurrentPath();
        boolean isEncFs = false;
        if (cp.isFile())
        {
            String fn = cp.getFile().getName();
            if (Config.CONFIG_FILENAME.equalsIgnoreCase(fn) || Config.CONFIG_FILENAME2.equalsIgnoreCase(fn))
            {
                Path parentPath = cp.getParentPath();
                if (parentPath != null)
                {
                    locationLocation.setCurrentPath(parentPath);
                    isEncFs = true;
                }
            }
        }
        else if (cp.isDirectory())
        {
            Path cfgPath = Config.getConfigFilePath(cp.getDirectory());
            if (cfgPath == null)
                throw new UserException("EncFs config file doesn't exist", R.string.encfs_config_file_not_found);
            isEncFs = true;
        }
        else
            throw new UserException("Wrong path", R.string.wrong_path);
        if (isEncFs)
            return new EncFsLocation(locationLocation, _context);
        else
            return createContainerBasedLocation(locationLocation);
    }

    protected ContainerLocation createContainerBasedLocation(Location locationLocation) throws Exception
    {
        Settings settings = UserSettings.getSettings(_context);
        return createContainerLocationBase(locationLocation, settings);
    }

    protected ContainerLocation createContainerLocationBase(Location locationLocation, Settings settings) throws IOException
    {
        String formatName = getArguments().getString(CreateContainerTaskFragmentBase.ARG_CONTAINER_FORMAT);
        if (formatName == null)
            formatName = "";
        return ContainerFormatter.createBaseContainerLocationFromFormatInfo(formatName, locationLocation, null, _context, settings);
    }
}
