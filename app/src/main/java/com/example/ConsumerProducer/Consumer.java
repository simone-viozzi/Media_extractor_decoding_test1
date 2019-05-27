package com.example.ConsumerProducer;

import android.util.Log;

import com.example.TimeClasses.ElapsedTime;

import java.util.concurrent.BlockingQueue;

@Deprecated
class Consumer implements Runnable
{
    private BlockingQueue queue;

    String id;

    ElapsedTime t;

    Consumer(BlockingQueue q, ElapsedTime t, String id)
    {
        queue = q;
        this.id = id;
        this.t = t;
    }

    public void run()
    {
        try
        {
            for (int i = 0; i < 300000; i++)
            {
                consume((Data) queue.take());
            }

        }
        catch (InterruptedException ex)
        {
            Log.e("consumer " + id, "consume(queue.take()); error");
        }

        t.Toc();

        Log.e("consumer " + id, "enlapsed time " + t.getElapsedTimeMillis());
    }


    private void consume(Data x)
    {
        Integer[] d = (Integer[])x.getItems();
        //Log.v("consumer " + id, ((Data<Integer>) x).getCounter() + " " + Arrays.toString(d));
        Log.v("consumer " + id, " " + ((Data<Integer>) x).getCounter());
    }
}