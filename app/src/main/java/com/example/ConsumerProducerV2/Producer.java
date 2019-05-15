package com.example.ConsumerProducerV2;

import android.support.v4.util.CircularArray;
import android.util.Log;

import com.example.TimeClasses.EnlapsedTime;

class Producer implements Runnable
{
    private CircularArray a;

    private int cont = 0;
    private EnlapsedTime t;
    private int n = 0;
    private Sync sync;


    Producer(CircularArray a, EnlapsedTime t, Sync sync, int n)
    {
        this.a = a;
        this.t = t;
        this.n = n;
        this.sync = sync;
    }


    public void run()
    {
        t.Tic();
        for (int i = 0; i < n;)
        {
            if (a.size() < 60)
            {
                if (a.size() < 2)
                {
                    sync.setSync(false);
                }
                else{
                    sync.setSync(true);
                }
                a.addLast(produce());
                i++;
            }
        }
        sync.setSync(true);

    }

    private Object produce()
    {
        Data d = new Data<Integer[]>();
        d.setCounter(cont++);

        Integer[] array = new Integer[50];

        for (int i = 0; i < array.length; i++)
        {
            array[i] = cont / (i + 1);
        }

        d.setItems(array);

        return d;
    }
}
