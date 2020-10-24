package com.igeltech.nevercrypt.android.locations.tasks;

import android.os.Bundle;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.errors.UserException;
import com.igeltech.nevercrypt.container.Container;
import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.ContainerFormatter;
import com.igeltech.nevercrypt.container.ContainerFormatterBase;
import com.igeltech.nevercrypt.container.LocationFormatter;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.fs.FileSystemInfo;
import com.igeltech.nevercrypt.fs.Path;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.Openable;

public abstract class CreateContainerTaskFragmentBase extends CreateLocationTaskFragment
{
    public static ContainerFormatInfo getContainerFormatByName(String name)
    {
        for (ContainerFormatInfo ci : Container.getSupportedFormats())
            if (ci.getFormatName().equals(name))
                return ci;
        return null;
    }

    public static final String ARG_CONTAINER_FORMAT = "com.igeltech.nevercrypt.android.CONTAINER_FORMAT";
    public static final String ARG_CIPHER_MODE_NAME = "com.igeltech.nevercrypt.android.CIPHER_MODE_NAME";
    public static final String ARG_HASHING_ALG = "com.igeltech.nevercrypt.android.HASHING_ALG";
    public static final String ARG_SIZE = "com.igeltech.nevercrypt.android.SIZE";
    public static final String ARG_FILL_FREE_SPACE = "com.igeltech.nevercrypt.android.FILL_FREE_SPACE";
    public static final String ARG_FILE_SYSTEM_TYPE = "com.igeltech.nevercrypt.android.FILE_SYSTEM_TYPE";

    @Override
    protected LocationFormatter createFormatter()
    {
        return new ContainerFormatter();
    }

    @Override
    protected void initFormatter(TaskState state, LocationFormatter formatter, SecureBuffer password) throws Exception
    {
        super.initFormatter(state, formatter, password);
        Bundle args = getArguments();
        ContainerFormatterBase cf = (ContainerFormatterBase)formatter;
        cf.setContainerFormat(getContainerFormatByName(args.getString(ARG_CONTAINER_FORMAT)));
        cf.setContainerSize(args.getInt(ARG_SIZE) * 1024L * 1024L);
        cf.setNumKDFIterations(args.getInt(Openable.PARAM_KDF_ITERATIONS, 0));
        FileSystemInfo fst = args.getParcelable(ARG_FILE_SYSTEM_TYPE);
        if(fst!=null)
            cf.setFileSystemType(fst);
        String encAlgName = args.getString(ARG_CIPHER_NAME);
        String encModeName = args.getString(ARG_CIPHER_MODE_NAME);
        if (encAlgName != null && encModeName != null)
            cf.setEncryptionEngine(encAlgName, encModeName);
        String hashAlgName = args.getString(ARG_HASHING_ALG);
        if (hashAlgName != null)
            cf.setHashFunc(hashAlgName);
        cf.enableFreeSpaceRand(args.getBoolean(ARG_FILL_FREE_SPACE));
    }

    @Override
    protected boolean checkParams(TaskState state, Location locationLocation) throws Exception
    {
        Bundle args = getArguments();
        Path path = locationLocation.getCurrentPath();
        if (path.exists() && path.isDirectory())
            throw new UserException(_context,
                    R.string.container_file_name_is_not_specified);
        if (args.getInt(ARG_SIZE) < 1)
            throw new UserException(getActivity(),
                    R.string.err_container_size_is_too_small);

        if (!getArguments().getBoolean(ARG_OVERWRITE, false))
        {
            if (path.exists()
                    && path.isFile()
                    && path.getFile().getSize() > 0)
            {
                state.setResult(RESULT_REQUEST_OVERWRITE);
                return false;
            }
        }
        return true;
    }

}
