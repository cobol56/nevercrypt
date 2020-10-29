package com.igeltech.nevercrypt.container;

import com.igeltech.nevercrypt.android.locations.EncFsLocation;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.fs.encfs.AlgInfo;
import com.igeltech.nevercrypt.fs.encfs.Config;
import com.igeltech.nevercrypt.fs.encfs.DataCodecInfo;
import com.igeltech.nevercrypt.fs.encfs.FS;
import com.igeltech.nevercrypt.fs.encfs.NameCodecInfo;
import com.igeltech.nevercrypt.locations.CryptoLocation;
import com.igeltech.nevercrypt.locations.Location;

import java.io.IOException;

public class EncFsFormatter extends LocationFormatter
{
    protected final Config _config = new Config();
    protected String _dataCodecName, _nameCodecName;

    public EncFsFormatter()
    {
        _config.initNew(_context);
    }

    public static AlgInfo findInfoByName(Config config, Iterable<? extends AlgInfo> supportedAlgs, String name)
    {
        for (AlgInfo info : supportedAlgs)
        {
            if (name.equals(info.getName()))
                return info.select(config);
        }
        throw new IllegalArgumentException("Unsupported codec: " + name);
    }

    public void setDataCodecName(String name)
    {
        _dataCodecName = name;
    }

    public void setNameCodecName(String name)
    {
        _nameCodecName = name;
    }

    public final Config getConfig()
    {
        return _config;
    }

    @Override
    protected CryptoLocation createLocation(Location location) throws IOException, ApplicationException
    {
        Path targetPath = location.getCurrentPath();
        if (targetPath.isFile())
        {
            Path tmp = targetPath.getParentPath();
            if (tmp != null)
            {
                targetPath = tmp;
                location.setCurrentPath(targetPath);
            }
        }
			/*
		if(!targetPath.isDirectory())
		{
			Path parentPath = targetPath.getParentPath();
			if(parentPath!=null)
			{
				String fn = PathUtil.getNameFromPath(targetPath);
				if(fn!=null)
					parentPath.getDirectory().createDirectory(fn);
			}
		}*/
        if (_dataCodecName != null)
            _config.setDataCodecInfo((DataCodecInfo) findInfoByName(_config, FS.getSupportedDataCodecs(), _dataCodecName));
        if (_nameCodecName != null)
            _config.setNameCodecInfo((NameCodecInfo) findInfoByName(_config, FS.getSupportedNameCodecs(), _nameCodecName));
        byte[] pd = _password == null ? new byte[0] : _password.getDataArray();
        try
        {
            return new EncFsLocation(location, new FS(targetPath, _config, pd), _context, UserSettings.getSettings(_context));
        }
        finally
        {
            SecureBuffer.eraseData(pd);
        }
    }
}
