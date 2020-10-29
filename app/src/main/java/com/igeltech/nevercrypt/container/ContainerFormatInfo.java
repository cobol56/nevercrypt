package com.igeltech.nevercrypt.container;

import com.igeltech.nevercrypt.exceptions.ApplicationException;
import com.igeltech.nevercrypt.fs.FileSystemInfo;
import com.igeltech.nevercrypt.fs.RandomAccessIO;

import java.io.IOException;

public interface ContainerFormatInfo
{
    String getFormatName();

    VolumeLayout getVolumeLayout();

    boolean hasHiddenContainerSupport();

    boolean hasKeyfilesSupport();

    boolean hasCustomKDFIterationsSupport();

    int getMaxPasswordLength();

    VolumeLayout getHiddenVolumeLayout();

    void formatContainer(RandomAccessIO io, VolumeLayout layout, FileSystemInfo fsInfo) throws IOException, ApplicationException;

    int getOpeningPriority();
}
