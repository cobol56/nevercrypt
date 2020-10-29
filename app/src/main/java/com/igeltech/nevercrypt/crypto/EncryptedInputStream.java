package com.igeltech.nevercrypt.crypto;

import com.igeltech.nevercrypt.container.EncryptedFileLayout;
import com.igeltech.nevercrypt.fs.util.TransInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class EncryptedInputStream extends TransInputStream
{
    protected final EncryptedFileLayout _layout;
    protected boolean _allowEmptyParts = true;

    public EncryptedInputStream(InputStream base, EncryptedFileLayout layout)
    {
        super(base, layout.getEngine().getFileBlockSize());
        _layout = layout;
    }

    public synchronized void close(boolean closeBase) throws IOException
    {
        try
        {
            super.close(closeBase);
        }
        finally
        {
            _layout.close();
            Arrays.fill(_buffer, (byte) 0);
        }
    }

    public final void setAllowEmptyParts(boolean val)
    {
        _allowEmptyParts = val;
    }

    @Override
    protected int transformBufferFromBase(byte[] baseBuffer, int offset, int count, long bufferPosition, byte[] dstBuffer) throws IOException
    {
        if (_allowEmptyParts && count == _bufferSize && EncryptedFile.isBufferEmpty(baseBuffer, offset, count))
            return count;
        FileEncryptionEngine ee = _layout.getEngine();
        _layout.setEncryptionEngineIV(ee, bufferPosition);
        try
        {
            ee.decrypt(baseBuffer, offset, count);
        }
        catch (EncryptionEngineException e)
        {
            throw new IOException(e);
        }
        return count;
    }
}
