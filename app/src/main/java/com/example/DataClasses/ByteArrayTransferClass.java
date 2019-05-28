package com.example.DataClasses;

import android.support.v4.util.CircularArray;
import android.util.Log;

import java.io.EOFException;


// TODO da rivedere le primitive di sincronizzazione perche' il consumer e' un sacco
//  piu veloce del producer e cio fa pensare a questa classe che il lavoro sia finito
//  basta mettere un boolean che dice quando il lavoro e' effettivamente finito
//  i'm so stupid!

@Deprecated
public class ByteArrayTransferClass
{
    private final String TAG = getClass().getSimpleName();
    private CircularArray<byte[]> circularArray;
    private int bufferCapacity;
    private boolean sync = false;
    private boolean firstTime = true;

    public ByteArrayTransferClass(int bufferCapacity)
    {
        circularArray = new CircularArray<>(bufferCapacity);
        this.bufferCapacity = bufferCapacity;
    }

    public void enqueue(byte[] data) throws ArrayStoreException
    {
        if (circularArray.size() < bufferCapacity)
        {
            sync = circularArray.size() >= 2;
            circularArray.addLast(data);
            firstTime = false;
        }
        else
        {
            Log.e(TAG, "buffer pieno");
            throw new ArrayStoreException("buffer pieno");
        }
    }

    public byte[] dequeue() throws ArrayStoreException, EOFException
    {
        if (circularArray.size() != 0)
        {
            if (sync)
            {
                return circularArray.popFirst();
            }
            else
            {
                //Log.e(TAG, "buffer troppo vuoto");
                throw new ArrayStoreException("buffer troppo vuoto, retry later");
            }
        }
        else if (!firstTime)
        {
            throw new EOFException("buffer vuoto, job ended");
        }
        else
        {
            //Log.e(TAG, "buffer troppo vuoto");
            throw new ArrayStoreException("buffer troppo vuoto, retry later");
        }
        //Log.e(TAG, "something goes awfully wrong");
        //return null;
    }

}
