package com.example.ConsumerProducerV3;

import com.example.DataClasses.ByteArrayTransferClass;
import com.example.TimeClasses.ElapsedTime;

import java.util.Arrays;

public class Producer implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private ElapsedTime t;
    private int n;
    private ByteArrayTransferClass buff;

    Producer(ElapsedTime t, int numberOfBuffersToBeTransferred,
             ByteArrayTransferClass buff)
    {
        this.t = t;
        n = numberOfBuffersToBeTransferred;
        this.buff = buff;
    }


    @Override
    public void run()
    {
        t.Tic();

        for (int i = 0; i < n; i++)
        {
            byte[] b = new byte[1024];
            Arrays.fill(b, (byte) i);
            try
            {
                buff.enqueue(b);
            }
            catch (ArrayStoreException e)
            {
                i--;
            }

        }

    }
}
