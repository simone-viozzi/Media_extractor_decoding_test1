package com.example.ConsumerProducer;

import android.util.Log;

import com.example.TimeClasses.ElapsedTime;

import java.util.concurrent.BlockingQueue;


@Deprecated
class Producer implements Runnable
{
    private BlockingQueue queue;

    int cont = 0;
    ElapsedTime t;

    Producer(BlockingQueue q, ElapsedTime t)
    {
        queue = q;
        this.t = t;
    }


    public void run()
    {
        t.Tic();
        try
        {
            for (int i = 0; i < 300000; i++)
            {
                queue.put(produce());
            }

        }
        catch (InterruptedException ex)
        {
            Log.e("Producer", "queue.put(produce()); error");
        }
    }

    private Object produce()
    {
        Data d = new Data<Integer[]>();
        d.setCounter(cont++);

        Integer[] array = new Integer[50];

        for (int i = 0; i < array.length; i++)
        {
            array[i] = cont / (i+1);
        }

        d.setItems(array);

        return d;
    }
}