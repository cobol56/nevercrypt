package com.igeltech.nevercrypt.container;

import com.igeltech.nevercrypt.fs.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Container extends ContainerBase
{
    private static final ContainerFormatInfo[] SUPPORTED_FORMATS = new ContainerFormatInfo[]{new com.igeltech.nevercrypt.truecrypt.FormatInfo(), new com.igeltech.nevercrypt.veracrypt.FormatInfo(), new com.igeltech.nevercrypt.luks.FormatInfo()};

    public Container(Path path)
    {
        this(path, null, null);
    }

    public Container(Path path, ContainerFormatInfo containerFormat, VolumeLayout layout)
    {
        super(path, containerFormat, layout);
    }

    public static List<ContainerFormatInfo> getSupportedFormats()
    {
        ArrayList<ContainerFormatInfo> al = new ArrayList<>();
        Collections.addAll(al, SUPPORTED_FORMATS);
        return al;
    }

    public static ContainerFormatInfo findFormatByName(String name)
    {
        return findFormatByName(getSupportedFormats(), name);
    }

    @Override
    protected List<ContainerFormatInfo> getFormats()
    {
        return getSupportedFormats();
    }
}



