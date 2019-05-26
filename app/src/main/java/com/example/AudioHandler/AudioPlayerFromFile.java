package com.example.AudioHandler;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioPlayerFromFile implements AudioPlayerInterface
{
    private MediaPlayer player;

    public AudioPlayerFromFile(String fileName) throws IOException
    {
        player = new MediaPlayer();
        player.setDataSource(fileName);
        player.prepare();
    }

    @Override
    public void startPlaying()
    {
        player.start();
    }

    @Override
    public void pausePlayer()
    {

    }

    @Override
    public void resumePlayer()
    {

    }

    @Override
    public void stopPlaying()
    {
        //
    }

}
