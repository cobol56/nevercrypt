package com.igeltech.nevercrypt.util;

import java.util.Iterator;

public abstract class IteratorConverter<S, T> implements Iterator<T>
{
    private final Iterator<? extends S> _srcIterator;

    protected IteratorConverter(Iterator<? extends S> srcIterator)
    {
        _srcIterator = srcIterator;
    }

    @Override
    public boolean hasNext()
    {
        return _srcIterator.hasNext();
    }

    @Override
    public T next()
    {
        return convert(_srcIterator.next());
    }

    @Override
    public void remove()
    {
        _srcIterator.remove();
    }

    public Iterator<? extends S> getSrcIter()
    {
        return _srcIterator;
    }

    protected abstract T convert(S src);
}
