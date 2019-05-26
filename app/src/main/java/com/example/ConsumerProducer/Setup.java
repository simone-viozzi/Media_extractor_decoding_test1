package com.example.ConsumerProducer;

import com.example.TimeClasses.EnlapsedTime;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


@Deprecated
public class Setup
{
    public void start()
    {
        BlockingQueue q = new ArrayBlockingQueue<Data<Integer[]>>(10, true);

        EnlapsedTime t = new EnlapsedTime();

        Producer p = new Producer(q, t);
        Consumer c1 = new Consumer(q, t, "c1");
        //Consumer c2 = new Consumer(q, t, "c2");

        new Thread(p).start();

        new Thread(c1).start();
        //new Thread(c2).start();
    }
}
