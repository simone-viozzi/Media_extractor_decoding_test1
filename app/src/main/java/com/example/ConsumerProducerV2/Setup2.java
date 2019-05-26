package com.example.ConsumerProducerV2;

import android.support.v4.util.CircularArray;

import com.example.TimeClasses.ElapsedTime;

@Deprecated
public class Setup2
{
    public void start(int n)
    {
        CircularArray a = new CircularArray<Data<Integer[]>>(63);
        Sync sync = new Sync();
        sync.setSync(false);

        ElapsedTime t = new ElapsedTime();

        Producer p = new Producer(a, t, sync, n);
        Consumer c1 = new Consumer(a, t, sync, n, "c1");
        //Consumer c2 = new Consumer(q, t, "c2");

        new Thread(p).start();

        new Thread(c1).start();
        //new Thread(c2).start();
    }
}

