package com.example.ConsumerProducerV4;

import com.example.DataClasses.ByteArrayTransferClassV2;
import com.example.TimeClasses.ElapsedTime;

public class Setup
{
    private final String TAG = getClass().getSimpleName();


    public void start() throws InterruptedException
    {
        ElapsedTime t = new ElapsedTime();
        ByteArrayTransferClassV2 buff = new ByteArrayTransferClassV2(32);

        Producer p = new Producer(t, buff);
        Consumer c = new Consumer(t, buff);

        Thread tP = new Thread(p);
        Thread tC = new Thread(c);
        tP.start();
        tC.start();

        Thread.sleep(1000);

        buff.setWorkDone(true);

    }
}
