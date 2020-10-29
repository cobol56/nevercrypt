package com.igeltech.nevercrypt.locations;

import com.igeltech.nevercrypt.container.Container;
import com.igeltech.nevercrypt.container.ContainerFormatInfo;

import java.io.IOException;
import java.util.List;

public interface ContainerLocation extends CryptoLocation
{
    @Override
    ExternalSettings getExternalSettings();

    Container getCryptoContainer() throws IOException;

    List<ContainerFormatInfo> getSupportedFormats();

    interface ExternalSettings extends CryptoLocation.ExternalSettings
    {
        String getContainerFormatName();

        void setContainerFormatName(String containerFormatName);

        String getEncEngineName();

        void setEncEngineName(String encEngineName);

        String getHashFuncName();

        void setHashFuncName(String hashFuncName);
    }
}
