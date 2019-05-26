package com.example.AudioHandler;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

public class AudioPlayerFromPCM implements AudioPlayerInterface
{
    private AudioTrack audioTrack;
    private int sampleRate;

    AudioPlayerFromPCM(int sampleRate)
    {
        this.sampleRate = sampleRate;
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioFormat audioFormat = new AudioFormat.Builder()
                .setSampleRate(this.sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .build();

        int bufferSizeInBytes = AudioTrack.getMinBufferSize(this.sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(audioAttributes, audioFormat, bufferSizeInBytes, AudioTrack.MODE_STREAM, 0);
    }

    /*
            audioTrack.play();

            short shortBuffer[] = new short[AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT)];


            while (!stopRequested)
            {
                readData(shortBuffer);
                audioTrack.write(shortBuffer, 0, shortBuffer.length, AudioTrack.WRITE_BLOCKING);
            }

     */

    /*
        to know how many Us of audio where played

        int Us = ( track.getPlaybackHeadPosition( ) / track.getSampleRate( ) );
     */

    @Override
    public void startPlaying()
    {

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

    }
}
