package com.example.DataClasses;

import android.util.Log;

import java.io.EOFException;
import java.util.Arrays;

public class ByteArrayPrinterClass implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private ByteArrayTransferClass buff;

    public ByteArrayPrinterClass(ByteArrayTransferClass buff)
    {
        this.buff = buff;
        Log.e(TAG, "ce so pure io");
    }

    @Override
    public void run()
    {
        Log.e(TAG, "ce so pure io v2");

        while (true)
        {
            try
            {
                //Log.e(TAG, "ce so pure io v3");
                byte[] b = buff.dequeue();
                String s = Arrays.toString(b);
                Log.w(TAG, "i dati ricevuti sono -> " + s);
            }
            catch (ArrayStoreException e)
            {
                //wwsw
            }
            catch (EOFException e)
            {
                Log.w(TAG, "lavoro finito");
                break;
            }

        }

    }
}
