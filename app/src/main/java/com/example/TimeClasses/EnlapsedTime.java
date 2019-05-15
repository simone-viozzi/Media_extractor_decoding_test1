package com.example.TimeClasses;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class EnlapsedTime
{
    long currTime = 0;


    public void Tic()
    {
        currTime = System.currentTimeMillis();
    }

    public long TocMillis()
    {
        return System.currentTimeMillis() - currTime;
    }

    public String TocSeconds()
    {

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format((System.currentTimeMillis() - currTime)/1000.0);
    }

}
