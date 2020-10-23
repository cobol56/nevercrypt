package com.igeltech.nevercrypt.locations;

import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.Container;

import java.io.IOException;
import java.util.List;

public interface ContainerLocation extends CryptoLocation
{
    interface ExternalSettings extends CryptoLocation.ExternalSettings
    {
        void setContainerFormatName(String containerFormatName);
        void setEncEngineName(String encEngineName);
        void setHashFuncName(String hashFuncName);
        String getContainerFormatName();
        String getEncEngineName();
        String getHashFuncName();
    }
    @Override
    ExternalSettings getExternalSettings();
    Container getCryptoContainer() throws IOException;
    List<ContainerFormatInfo> getSupportedFormats();
}
