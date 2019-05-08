package com.example.ConsumerProducer;

import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable
{
    private final BlockingQueue queue;

    int cont = 0;

    Producer(BlockingQueue q)
    {
        queue = q;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                queue.put(produce());
            }
        }
        catch (InterruptedException ex)
        {
            Log.e("Producer", "queue.put(produce()); error");
        }
    }

    Object produce()
    {
        Data d = new Data<Integer>();
        d.setCounter(cont++);

        Integer[] array = new Integer[50];

        for (int i = 0; i < array.length; i++)
        {
            array[i] = cont * i;
        }

        d.setItems(array);

        return d;
    }
}