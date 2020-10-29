package com.igeltech.nevercrypt.locations;

public interface OMLocation extends Openable
{
    @Override
    ExternalSettings getExternalSettings();

    boolean isOpenOrMounted();

    interface ExternalSettings extends Location.ExternalSettings
    {
        byte[] getPassword();

        void setPassword(byte[] password);

        int getCustomKDFIterations();

        void setCustomKDFIterations(int val);

        boolean hasPassword();
    }
}
