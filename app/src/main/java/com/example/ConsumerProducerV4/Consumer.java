package com.example.ConsumerProducerV4;

import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;
import com.example.TimeClasses.ElapsedTime;

public class Consumer implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private ElapsedTime t;
    private ByteArrayTransferClassV2 buff;

    Consumer(ElapsedTime t,
             ByteArrayTransferClassV2 buff)
    {
        this.t = t;
        this.buff = buff;
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (buff.isOkToDequeue())
            {
                byte[] b = buff.dequeue();
                //Log.d(TAG, "i dati sono ricevuti -> " + Arrays.toString(b));
            }
            else if (buff.isWorkDone())
            {
                break;
            }
        }

        t.Toc();

        Log.d(TAG, "transferred in " + t.getElapsedTimeMillis() + " millis");
    }

}
