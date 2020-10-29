package com.igeltech.nevercrypt.fs.util;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.igeltech.nevercrypt.fs.FSRecord;
import com.igeltech.nevercrypt.fs.FileSystem;
import com.igeltech.nevercrypt.fs.RandomAccessIO;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ActivityTrackingFSWrapper extends FileSystemWrapper
{
    protected long _lastActivityTime;
    private ChangeListener _changesListener;

    public ActivityTrackingFSWrapper(FileSystem baseFs)
    {
        super(baseFs);
        _lastActivityTime = SystemClock.elapsedRealtime();
    }

    @Override
    public com.igeltech.nevercrypt.fs.Path getRootPath() throws IOException
    {
        return new Path(getBase().getRootPath());
    }

    @Override
    public com.igeltech.nevercrypt.fs.Path getPath(String pathString) throws IOException
    {
        return new Path(getBase().getPath(pathString));
    }

    public void setChangesListener(ChangeListener l)
    {
        _changesListener = l;
    }

    public long getLastActivityTime()
    {
        return _lastActivityTime;
    }

    protected com.igeltech.nevercrypt.fs.Path getPathFromBasePath(com.igeltech.nevercrypt.fs.Path basePath) throws IOException
    {
        return basePath == null ? null : new Path(basePath);
    }

    private void beforeMove(FSRecord srcRecord, com.igeltech.nevercrypt.fs.Directory newParent) throws IOException
    {
        _lastActivityTime = SystemClock.elapsedRealtime();
        if (_changesListener != null)
        {
            _changesListener.beforeRemoval(srcRecord.getPath());
            com.igeltech.nevercrypt.fs.Path dst;
            try
            {
                dst = newParent.getPath().combine(srcRecord.getName());
            }
            catch (IOException e)
            {
                dst = null;
            }
            if (dst != null)
                _changesListener.beforeModification(dst);
        }
    }

    private void afterMove(com.igeltech.nevercrypt.fs.Path srcPath, FSRecordWrapper srcRecord) throws IOException
    {
        if (_changesListener != null)
        {
            _changesListener.afterRemoval(srcPath);
            _changesListener.afterModification(srcRecord.getPath());
        }
    }

    private void beforeDelete(FSRecord srcRecord) throws IOException
    {
        _lastActivityTime = SystemClock.elapsedRealtime();
        if (_changesListener != null)
            _changesListener.beforeRemoval(srcRecord.getPath());
    }

    private void afterDelete(FSRecord srcRecord)
    {
        if (_changesListener != null)
            _changesListener.afterRemoval(srcRecord.getPath());
    }

    public interface ChangeListener
    {
        void beforeRemoval(com.igeltech.nevercrypt.fs.Path p) throws IOException;

        void afterRemoval(com.igeltech.nevercrypt.fs.Path p);

        void beforeModification(com.igeltech.nevercrypt.fs.Path p) throws IOException;

        void afterModification(com.igeltech.nevercrypt.fs.Path p);
    }

    protected class Path extends PathWrapper
    {
        public Path(com.igeltech.nevercrypt.fs.Path path)
        {
            super(ActivityTrackingFSWrapper.this, path);
        }

        @Override
        public com.igeltech.nevercrypt.fs.File getFile() throws IOException
        {
            return new File(this, getBase().getFile());
        }

        @Override
        public com.igeltech.nevercrypt.fs.Directory getDirectory() throws IOException
        {
            return new Directory(this, getBase().getDirectory());
        }

        @Override
        protected com.igeltech.nevercrypt.fs.Path getPathFromBasePath(com.igeltech.nevercrypt.fs.Path basePath) throws IOException
        {
            return ActivityTrackingFSWrapper.this.getPathFromBasePath(basePath);
        }
    }

    protected class File extends FileWrapper
    {
        public File(Path path, com.igeltech.nevercrypt.fs.File base)
        {
            super(path, base);
        }

        @Override
        public void moveTo(com.igeltech.nevercrypt.fs.Directory newParent) throws IOException
        {
            com.igeltech.nevercrypt.fs.Path srcPath = getPath();
            beforeMove(this, newParent);
            super.moveTo(newParent);
            afterMove(srcPath, this);
        }

        @Override
        protected com.igeltech.nevercrypt.fs.Path getPathFromBasePath(com.igeltech.nevercrypt.fs.Path basePath) throws IOException
        {
            return ActivityTrackingFSWrapper.this.getPathFromBasePath(basePath);
        }

        @Override
        public void delete() throws IOException
        {
            beforeDelete(this);
            super.delete();
            afterDelete(this);
        }

        @Override
        public InputStream getInputStream() throws IOException
        {
            final InputStream in = super.getInputStream();
            return new FilterInputStream(in)
            {
                @Override
                public int read() throws IOException
                {
                    _lastActivityTime = SystemClock.elapsedRealtime();
                    return in.read();
                }

                @Override
                public int read(@NonNull byte[] b, int off, int len) throws IOException
                {
                    _lastActivityTime = SystemClock.elapsedRealtime();
                    return in.read(b, off, len);
                }
            };
        }

        @Override
        public OutputStream getOutputStream() throws IOException
        {
            final OutputStream out = super.getOutputStream();
            return new FilterOutputStream(out)
            {
                private boolean _isChanged;

                @Override
                public void write(int b) throws IOException
                {
                    _lastActivityTime = SystemClock.elapsedRealtime();
                    if (!_isChanged && _changesListener != null)
                        _changesListener.beforeModification(getPath());
                    out.write(b);
                    _isChanged = true;
                }

                @Override
                public void write(@NonNull byte[] b, int off, int len) throws IOException
                {
                    _lastActivityTime = SystemClock.elapsedRealtime();
                    if (!_isChanged && _changesListener != null)
                        _changesListener.beforeModification(getPath());
                    out.write(b, off, len);
                    _isChanged = true;
                }

                public void close() throws IOException
                {
                    super.close();
                    if (_changesListener != null && _isChanged)
                        _changesListener.afterModification(getPath());
                }
            };
        }

        @Override
        public RandomAccessIO getRandomAccessIO(AccessMode accessMode) throws IOException
        {
            return new ActivityTrackingFileIO(super.getRandomAccessIO(accessMode), getPath());
        }
    }

    protected class Directory extends DirectoryWrapper
    {
        public Directory(Path path, com.igeltech.nevercrypt.fs.Directory base)
        {
            super(path, base);
        }

        @Override
        public void moveTo(com.igeltech.nevercrypt.fs.Directory newParent) throws IOException
        {
            com.igeltech.nevercrypt.fs.Path srcPath = getPath();
            beforeMove(this, newParent);
            super.moveTo(newParent);
            afterMove(srcPath, this);
        }

        @Override
        protected com.igeltech.nevercrypt.fs.Path getPathFromBasePath(com.igeltech.nevercrypt.fs.Path basePath) throws IOException
        {
            return ActivityTrackingFSWrapper.this.getPathFromBasePath(basePath);
        }

        @Override
        public void delete() throws IOException
        {
            beforeDelete(this);
            super.delete();
            afterDelete(this);
        }

        @Override
        public com.igeltech.nevercrypt.fs.File createFile(String name) throws IOException
        {
            _lastActivityTime = SystemClock.elapsedRealtime();
            com.igeltech.nevercrypt.fs.File f = super.createFile(name);
            if (_changesListener != null)
                _changesListener.afterModification(f.getPath());
            return f;
        }

        @Override
        public com.igeltech.nevercrypt.fs.Directory createDirectory(String name) throws IOException
        {
            _lastActivityTime = SystemClock.elapsedRealtime();
            com.igeltech.nevercrypt.fs.Directory f = super.createDirectory(name);
            if (_changesListener != null)
                _changesListener.afterModification(f.getPath());
            return f;
        }

        @Override
        public Directory.Contents list() throws IOException
        {
            _lastActivityTime = SystemClock.elapsedRealtime();
            return super.list();
        }
    }

    protected class ActivityTrackingFileIO extends RandomAccessIOWrapper
    {
        private final com.igeltech.nevercrypt.fs.Path _path;
        private boolean _isChanged;

        public ActivityTrackingFileIO(RandomAccessIO base, com.igeltech.nevercrypt.fs.Path path) throws IOException
        {
            super(base);
            _path = path;
        }

        @Override
        public int read() throws IOException
        {
            _lastActivityTime = SystemClock.elapsedRealtime();
            return super.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException
        {
            _lastActivityTime = SystemClock.elapsedRealtime();
            return super.read(b, off, len);
        }

        @Override
        public void write(int b) throws IOException
        {
            _lastActivityTime = SystemClock.elapsedRealtime();
            if (!_isChanged && _changesListener != null)
                _changesListener.beforeModification(_path);
            super.write(b);
            _isChanged = true;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            _lastActivityTime = SystemClock.elapsedRealtime();
            if (!_isChanged && _changesListener != null)
                _changesListener.beforeModification(_path);
            super.write(b, off, len);
            _isChanged = true;
        }

        @Override
        public void close() throws IOException
        {
            super.close();
            if (_changesListener != null && _isChanged)
                _changesListener.afterModification(_path);
        }
    }
}
