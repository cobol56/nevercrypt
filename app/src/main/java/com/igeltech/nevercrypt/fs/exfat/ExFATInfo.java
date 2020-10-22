package com.igeltech.nevercrypt.fs.exfat;

import android.os.Parcel;

import com.igeltech.nevercrypt.fs.FileSystem;
import com.igeltech.nevercrypt.fs.FileSystemInfo;
import com.igeltech.nevercrypt.fs.RandomAccessIO;

import java.io.IOException;


public class ExFATInfo extends FileSystemInfo
{
    public static final Creator<ExFATInfo> CREATOR = new Creator<ExFATInfo>()
    {
        @Override
        public ExFATInfo createFromParcel(Parcel in)
        {
            return new ExFATInfo();
        }

        @Override
        public ExFATInfo[] newArray(int size)
        {
            return new ExFATInfo[size];
        }
    };

    public static final String NAME = "ExFAT";

    @Override
    public String getFileSystemName()
    {
        return NAME;
    }

    @Override
    public void makeNewFileSystem(RandomAccessIO img) throws IOException
    {
        ExFat.makeNewFS(img);
    }

    @Override
    public FileSystem openFileSystem(RandomAccessIO img, boolean readOnly) throws IOException
    {
        return new ExFat(img, readOnly);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
    }
}
