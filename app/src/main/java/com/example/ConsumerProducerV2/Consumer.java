package com.example.ConsumerProducerV2;

import android.support.v4.util.CircularArray;
import android.util.Log;

import com.example.TimeClasses.ElapsedTime;

@Deprecated
class Consumer implements Runnable
{
    private CircularArray a;
    private String id;
    private ElapsedTime t;
    private int n = 0;
    private Sync sync;

    Consumer(CircularArray a, ElapsedTime t, Sync sync, int n, String id)
    {
        this.a = a;
        this.id = id;
        this.t = t;
        this.n = n;
        this.sync = sync;
    }

    public void run()
    {
        for (int i = 0; i < n;)
        {
            if (sync.isSync() && (a.size() != 0))
            {
                Log.w("c1", "size = " + a.size());
                consume((Data) a.popFirst());
                i++;
            }
        }
        t.Toc();

        Log.e("consumer " + id, "enlapsed time " + t.getElapsedTimeMillis());
    }


    private void consume(Data x)
    {
        Integer[] d = (Integer[]) x.getItems();
        //Log.v("consumer " + id, ((Data<Integer>) x).getCounter() + " " + Arrays.toString(d));
        Log.v("consumer " + id, " " + ((Data<Integer>) x).getCounter());
    }
}