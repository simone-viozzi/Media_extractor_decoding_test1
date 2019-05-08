package com.example.ConsumerProducer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Setup
{
    public void start()
    {
        BlockingQueue q = new ArrayBlockingQueue<Data>(10, true);

        Producer p = new Producer(q);
        Consumer c1 = new Consumer(q, "c1");
        Consumer c2 = new Consumer(q, "c2");

        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }
}
