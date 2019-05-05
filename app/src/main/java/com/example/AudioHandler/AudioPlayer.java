package com.example.AudioHandler;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioPlayer
{
    private MediaPlayer player;

    public AudioPlayer()
    {
        player = new MediaPlayer();
    }


    public void startPlaying(String fileName) throws IOException
    {
        player.setDataSource(fileName);
        player.prepare();
        player.start();
    }

    public void stopPlaying()
    {
        //
    }

}
