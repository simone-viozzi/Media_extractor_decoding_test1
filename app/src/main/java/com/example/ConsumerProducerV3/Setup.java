package com.example.ConsumerProducerV3;

import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClass;
import com.example.TimeClasses.ElapsedTime;

public class Setup
{
    private final String TAG = getClass().getSimpleName();


    public void start(int numberOfBuffersToBeTransferred) throws InterruptedException
    {
        ElapsedTime t = new ElapsedTime();
        ByteArrayTransferClass buff = new ByteArrayTransferClass(512);

        Producer p = new Producer(t, numberOfBuffersToBeTransferred, buff);
        Consumer c = new Consumer(t, numberOfBuffersToBeTransferred, buff);

        Thread tP = new Thread(p);
        Thread tC = new Thread(c);
        tP.start();
        tC.start();

        tC.join();
        tP.join();

        Log.d(TAG, "transferred " + numberOfBuffersToBeTransferred + " buffer of 1024 byte " + " in " + t.getElapsedTimeMillis() + " millis");
    }

}
