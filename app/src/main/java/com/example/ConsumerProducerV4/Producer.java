package com.example.ConsumerProducerV4;

import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;
import com.example.TimeClasses.ElapsedTime;

import java.util.Arrays;

public class Producer implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private ElapsedTime t;
    private ByteArrayTransferClassV2 buff;

    Producer(ElapsedTime t,
             ByteArrayTransferClassV2 buff)
    {
        this.t = t;
        this.buff = buff;
    }


    @Override
    public void run()
    {
        t.Tic();
        int n = 0;
        while (!buff.isWorkDone())
        {
            if (buff.isOkToEnqueue())
            {
                byte[] b = new byte[1024];
                Arrays.fill(b, (byte) n);
                //Log.d(TAG, "i dati sono inviati  -> " + Arrays.toString(b));
                buff.enqueue(b);
                n++;
            }
        }

        Log.d(TAG, "transferred " + n + " buffer of 1024 byte");

    }
}
