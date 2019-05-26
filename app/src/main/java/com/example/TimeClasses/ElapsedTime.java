package com.example.TimeClasses;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ElapsedTime
{
    private long currTime = 0;
    private long elapsedTime;


    public void Tic()
    {
        currTime = System.currentTimeMillis();
    }

    public void Toc()
    {
        elapsedTime = System.currentTimeMillis() - currTime;
    }


    public String getElapsedTimeMillis()
    {
        return elapsedTime + "";
    }

    public String getElapsedTimeSecond()
    {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(elapsedTime);
    }
}
