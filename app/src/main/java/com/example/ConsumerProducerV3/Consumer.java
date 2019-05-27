package com.example.ConsumerProducerV3;

import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClass;
import com.example.TimeClasses.ElapsedTime;

import java.io.EOFException;

public class Consumer implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private ElapsedTime t;
    private int n;
    private ByteArrayTransferClass buff;

    Consumer(ElapsedTime t, int numberOfBuffersToBeTransferred,
             ByteArrayTransferClass buff)
    {
        this.t = t;
        n = numberOfBuffersToBeTransferred;
        this.buff = buff;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < n; i++)
        {
            try
            {
                byte[] b = buff.dequeue();
                //Log.d(TAG, "first element: " + b[0] +", last element: " + b[1023]);
            }
            catch (ArrayStoreException e)
            {
                i--;
            }
            catch (EOFException e)
            {
                Log.d(TAG, "lavoro finito");
                //e.printStackTrace();
            }

        }

        t.Toc();
    }
}
