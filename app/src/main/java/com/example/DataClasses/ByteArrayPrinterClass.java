package com.example.DataClasses;

import android.util.Log;

import java.util.Arrays;

public class ByteArrayPrinterClass implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private ByteArrayTransferClassV2 buff;

    public ByteArrayPrinterClass(ByteArrayTransferClassV2 buff)
    {
        this.buff = buff;
    }

    @Override
    public void run()
    {
        while (!buff.isWorkDone())
        {
            while (!buff.isOkToDequeue())
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            byte[] b = buff.dequeue();
            String s = Arrays.toString(b);
            Log.w(TAG, "i dati ricevuti sono -> " + s);
        }

    }
}
