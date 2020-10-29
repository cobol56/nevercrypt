package com.igeltech.nevercrypt.locations;

import com.igeltech.nevercrypt.fs.util.ContainerFSWrapper;

import java.io.IOException;

public interface CryptoLocation extends OMLocation
{
    @Override
    ExternalSettings getExternalSettings();

    InternalSettings getInternalSettings();

    void applyInternalSettings() throws IOException;

    void readInternalSettings() throws IOException;

    void writeInternalSettings() throws IOException;

    long getLastActivityTime();

    Location getLocation();

    @Override
    ContainerFSWrapper getFS() throws IOException;

    @Override
    CryptoLocation copy();

    interface ExternalSettings extends OMLocation.ExternalSettings
    {
        boolean shouldOpenReadOnly();

        void setOpenReadOnly(boolean val);

        int getAutoCloseTimeout();

        void setAutoCloseTimeout(int timeout);
    }

    interface InternalSettings
    {
    }
}
