package com.igeltech.nevercrypt.fs.encfs;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.fs.File;
import com.igeltech.nevercrypt.fs.util.DirectoryWrapper;
import com.igeltech.nevercrypt.fs.util.FilteredIterator;
import com.igeltech.nevercrypt.fs.util.StringPathUtil;
import com.igeltech.nevercrypt.util.IteratorConverter;

import java.io.IOException;
import java.util.Iterator;

public class Directory extends DirectoryWrapper
{
    public Directory(Path path, com.igeltech.nevercrypt.fs.Directory realDir) throws IOException
    {
        super(path, realDir);
    }

    @Override
    public String getName() throws IOException
    {
        return getPath().getDecodedPath().getFileName();
    }

    @Override
    public Contents list() throws IOException
    {
        final Contents contents = getBase().list();
        return new Contents()
        {
            @Override
            public void close() throws IOException
            {
                contents.close();
            }

            @Override
            public Iterator<com.igeltech.nevercrypt.fs.Path> iterator()
            {
                return new FilteringIterator(new Directory.DirIterator(getPath().getFileSystem(), contents.iterator()));
            }
        };
    }

    @Override
    public Path getPath()
    {
        return (Path) super.getPath();
    }

    @Override
    public void rename(String newName) throws IOException
    {
        if (getPath().getNamingCodecInfo().useChainedNamingIV() || getPath().getFileSystem().getConfig().useExternalFileIV())
            throw new UnsupportedOperationException();
        StringPathUtil newEncodedPath = getPath().getParentPath().calcCombinedEncodedParts(newName);
        super.rename(newEncodedPath.getFileName());
    }

    @Override
    public File createFile(String name) throws IOException
    {
        StringPathUtil decodedPath = getPath().getDecodedPath();
        if (decodedPath != null)
            decodedPath = decodedPath.combine(name);
        StringPathUtil newEncodedPath = getPath().calcCombinedEncodedParts(name);
        com.igeltech.nevercrypt.fs.encfs.File res = (com.igeltech.nevercrypt.fs.encfs.File) super.createFile(newEncodedPath.getFileName());
        res.getPath().setEncodedPath(newEncodedPath);
        if (decodedPath != null)
            res.getPath().setDecodedPath(decodedPath);
        res.getOutputStream().close();
        return res;
    }

    @Override
    public com.igeltech.nevercrypt.fs.Directory createDirectory(String name) throws IOException
    {
        StringPathUtil decodedPath = getPath().getDecodedPath();
        if (decodedPath != null)
            decodedPath = decodedPath.combine(name);
        StringPathUtil newEncodedPath = getPath().calcCombinedEncodedParts(name);
        Directory res = (Directory) super.createDirectory(newEncodedPath.getFileName());
        res.getPath().setEncodedPath(newEncodedPath);
        if (decodedPath != null)
            res.getPath().setDecodedPath(decodedPath);
        return res;
    }

    @Override
    public void moveTo(com.igeltech.nevercrypt.fs.Directory dst) throws IOException
    {
        if (getPath().getNamingCodecInfo().useChainedNamingIV() || getPath().getFileSystem().getConfig().useExternalFileIV())
            throw new UnsupportedOperationException();
        super.moveTo(dst);
    }

    @Override
    protected com.igeltech.nevercrypt.fs.Path getPathFromBasePath(com.igeltech.nevercrypt.fs.Path basePath) throws IOException
    {
        return getPath().getFileSystem().getPathFromRealPath(basePath);
    }

    private static class DirIterator extends IteratorConverter<com.igeltech.nevercrypt.fs.Path, Path>
    {
        private final FS _fs;

        protected DirIterator(FS fs, Iterator<? extends com.igeltech.nevercrypt.fs.Path> srcIterator)
        {
            super(srcIterator);
            _fs = fs;
        }

        @Override
        protected Path convert(com.igeltech.nevercrypt.fs.Path src)
        {
            try
            {
                return _fs.getPathFromRealPath(src);
            }
            catch (IOException e)
            {
                Logger.log(e);
                return null;
            }
        }
    }

    private static class FilteringIterator extends FilteredIterator<com.igeltech.nevercrypt.fs.Path>
    {
        public FilteringIterator(Iterator<Path> base)
        {
            super(base);
        }

        @Override
        protected boolean isValidItem(com.igeltech.nevercrypt.fs.Path item)
        {
            try
            {
                Path p = (Path) item;
                return !((p.getParentPath().isRootDirectory() && Config.CONFIG_FILENAME.equals(p.getEncodedPath().getFileName())) || p.getDecodedPath() == null);
            }
            catch (Throwable e)
            {
                Logger.log(e);
                return false;
            }
        }
    }
}
