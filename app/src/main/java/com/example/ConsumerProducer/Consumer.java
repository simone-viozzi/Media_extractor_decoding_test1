package com.example.ConsumerProducer;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

class Consumer implements Runnable
{
    private final BlockingQueue queue;

    String id;

    Consumer(BlockingQueue q, String id)
    {
        queue = q;
        this.id = id;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                consume((Data)queue.take());
            }
        }
        catch (InterruptedException ex)
        {
            Log.e("consumer " + id, "consume(queue.take()); error");
        }
    }

    //// da testare 
    void consume(Data x)
    {
        Integer[] d = (Integer[])x.getItems();
        Log.v("consumer " + id, ((Data<Integer>) x).getCounter() + (d.toString()));
    }
}