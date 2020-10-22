package com.igeltech.nevercrypt.locations;

import com.igeltech.nevercrypt.container.ContainerFormatInfo;
import com.igeltech.nevercrypt.container.EdsContainer;

import java.io.IOException;
import java.util.List;

public interface ContainerLocation extends EDSLocation
{
    interface ExternalSettings extends EDSLocation.ExternalSettings
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
    EdsContainer getEdsContainer() throws IOException;
    List<ContainerFormatInfo> getSupportedFormats();
}
