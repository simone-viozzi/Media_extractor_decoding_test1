package com.example.DataClasses;

import android.support.v4.util.CircularArray;
import android.util.Log;

public class ByteArrayTransferClassV2
{
    private final String TAG = getClass().getSimpleName();
    private CircularArray<byte[]> circularArray;
    private int bufferCapacity;
    private boolean workDone = false;
    private long transeredBytes = 0;

    public ByteArrayTransferClassV2(int bufferCapacity)
    {
        circularArray = new CircularArray<>(bufferCapacity);
        this.bufferCapacity = bufferCapacity;
    }

    public void enqueue(byte[] data) throws ArrayStoreException
    {
        if (circularArray.size() < bufferCapacity)
        {
            transeredBytes += 1024;
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
            if (b == null) // TODO b non deve essere mai nullo!!!!! soprattutto perche non carico mai un valore null nell'array.
            {
                Log.e(TAG, "perche cazzo questo mi da un buffer vuoto???");
                throw new NullPointerException("perche??");
            }
            return b;
        }
    }

    public boolean isWorkDone()
    {
        return (workDone & (circularArray.size() == 0));
    }

    public void setWorkDone(boolean workDone)
    {
        Log.d(TAG, "transfered " + transeredBytes + " bytes");
        this.workDone = workDone;
    }

    public boolean isOkToEnqueue()
    {
        return (circularArray.size() < bufferCapacity);
    }

    public boolean isOkToDequeue()
    {
        return (circularArray.size() > 0);
    }
}
