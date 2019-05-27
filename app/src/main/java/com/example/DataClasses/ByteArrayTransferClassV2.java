package com.example.DataClasses;

import android.support.v4.util.CircularArray;
import android.util.Log;

public class ByteArrayTransferClassV2
{
    /*
     *  to sleep
     *  Thread.sleep(milliseconds);
     * */


    private final String TAG = getClass().getSimpleName();
    private CircularArray<byte[]> circularArray;
    private int bufferCapacity;
    private boolean workDone = false;

    public ByteArrayTransferClassV2(int bufferCapacity)
    {
        circularArray = new CircularArray<>(bufferCapacity);
        this.bufferCapacity = bufferCapacity;
    }

    public void enqueue(byte[] data) throws ArrayStoreException
    {
        if (circularArray.size() < bufferCapacity)
        {
            circularArray.addLast(data);
        }
        else
        {
            Log.e(TAG, "sbatto sopra");
            throw new ArrayStoreException("buffer pieno");
        }
    }

    public byte[] dequeue() throws ArrayStoreException
    {
        if (circularArray.size() == 0)
        {
            Log.e(TAG, "sbatto sotto");
            throw new ArrayStoreException("buffer troppo vuoto, retry later");
        }
        else
        {
            byte[] b = circularArray.popFirst();
            if (b == null)
            {
                Log.e(TAG, "perche cazzo questo mi da un buffer vuoto???");
            }
            return b;
        }
    }

    public boolean isWorkDone()
    {
        //Log.e(TAG, "workDone? -> " + (workDone & (circularArray.size() == 0)));
        return (workDone & (circularArray.size() == 0));
    }

    public void setWorkDone(boolean workDone)
    {
        this.workDone = workDone;
    }

    public boolean isOkToEnqueue()
    {
        return (circularArray.size() < bufferCapacity);
    }

    public boolean isOkToDequeue()
    {
        //Log.w(TAG, "isOkToDequeue? -> " + (circularArray.size() > 0));
        return (circularArray.size() > 0);
    }
}
