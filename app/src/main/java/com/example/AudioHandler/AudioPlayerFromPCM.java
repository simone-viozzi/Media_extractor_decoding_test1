package com.example.AudioHandler;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;

public class AudioPlayerFromPCM implements AudioPlayerInterface
{
    private final String TAG = getClass().getSimpleName();
    private AudioTrack audioTrack;
    private int sampleRate;
    private ByteArrayTransferClassV2 buff;

    public AudioPlayerFromPCM(int sampleRate, ByteArrayTransferClassV2 buff)
    {
        this.sampleRate = sampleRate;
        this.buff = buff;
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        sampleRate = 44100 / 2;

        AudioFormat audioFormat = new AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .build();

        int bufferSizeInBytes = AudioTrack.getMinBufferSize(this.sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        //Log.e(TAG, "bufferSizeInBytes = " + bufferSizeInBytes);
        //Log.e(TAG, "sampleRate = " + sampleRate);

        bufferSizeInBytes = 1024;


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
        //Log.e(TAG, "startPlaying");
        audioTrack.play();
        while (!buff.isWorkDone())
        {
            while (!buff.isOkToDequeue())
            {
            }
            byte[] b = buff.dequeue();
            //Log.w(TAG, "dati scaricati");

            if (b == null)
            {
                Log.e(TAG, "perche cazzo b e' null?");
            }

            audioTrack.write(b, 0, b.length, AudioTrack.WRITE_BLOCKING);
        }
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
