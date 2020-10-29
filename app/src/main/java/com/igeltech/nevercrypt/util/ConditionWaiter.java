package com.igeltech.nevercrypt.util;

public class ConditionWaiter extends Thread
{
    private final Object _syncer;
    private final ICondition _condition;
    private int _timeout = 5000, _sleepTimeout = 200, _numRetries;
    private boolean _fin;
    private boolean _result;

    public ConditionWaiter(ICondition condition, Object syncer)
    {
        _condition = condition;
        _syncer = syncer;
    }

    public static boolean waitFor(ICondition condition)
    {
        return waitFor(condition, 5000);
    }

    public static boolean waitFor(ICondition condition, int timeout)
    {
        Object syncer = new Object();
        ConditionWaiter waiter = new ConditionWaiter(condition, syncer);
        waiter.setTimeout(timeout);
        synchronized (syncer)
        {
            waiter.start();
            try
            {
                syncer.wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return waiter.getResult();
    }

    public static boolean waitFor(ICondition condition, int numRetries, int sleepTimeout)
    {
        Object syncer = new Object();
        ConditionWaiter waiter = new ConditionWaiter(condition, syncer);
        waiter.setTimeout(0);
        waiter.setSleepTimeout(sleepTimeout);
        waiter.setNumRetries(numRetries);
        synchronized (syncer)
        {
            waiter.start();
            try
            {
                syncer.wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return waiter.getResult();
    }

    public void setTimeout(int timeout)
    {
        _timeout = timeout;
    }

    public void setSleepTimeout(int sleepTimeout)
    {
        _sleepTimeout = sleepTimeout;
    }

    public void setNumRetries(int numRetries)
    {
        _numRetries = numRetries;
    }

    public boolean getResult()
    {
        return _result;
    }

    @Override
    public void run()
    {
        _result = false;
        int sleepTime = 0;
        int retr = 0;
        try
        {
            do
            {
                _result = _condition.isTrue();
                if (!_result)
                {
                    try
                    {
                        sleep(_sleepTimeout);
                    }
                    catch (InterruptedException e)
                    {
                    }
                    sleepTime += _sleepTimeout;
                    retr++;
                }
            } while (!_fin && !_result && (_timeout == 0 || (_timeout > 0 && sleepTime < _timeout)) && (_numRetries == 0 || (_numRetries > 0 && retr < _numRetries)));
        }
        finally
        {
            synchronized (_syncer)
            {
                _syncer.notify();
            }
        }
    }

    public void fin()
    {
        _fin = true;
    }

    public interface ICondition
    {
        boolean isTrue();
    }
}
