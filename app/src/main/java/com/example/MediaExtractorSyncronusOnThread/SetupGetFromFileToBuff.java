package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;

import java.io.IOException;

public class SetupGetFromFileToBuff
{
    private final String TAG = getClass().getSimpleName();

    private String path;
    private ByteArrayTransferClassV2 buff;
    private MediaCodec decoder;
    private FromFileToCodec fromFileToCodec;
    private FromCodecToBuff fromCodecToBuff;


    public SetupGetFromFileToBuff(String path, ByteArrayTransferClassV2 buff)
    {
        this.path = path;

        //this.path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/test_audio_file/audio2.m4a";
        //Log.v(TAG, "path: " + path);
        this.buff = buff;
    }

    public void configure() throws IOException
    {
        MediaExtractor extractor = new MediaExtractor();


        extractor.setDataSource(path);
        Log.v(TAG, "setted path");

        int numTracks = extractor.getTrackCount();
        Log.v(TAG, "numTracks: " + numTracks);

        MediaFormat format = extractor.getTrackFormat(0);
        String mAudioKeyMine = format.getString(MediaFormat.KEY_MIME);
        Log.v(TAG, "format: " + format + ", mime: " + mAudioKeyMine);

        extractor.selectTrack(0);

        decoder = MediaCodec.createDecoderByType(mAudioKeyMine);
        decoder.configure(format, null, null, 0);

        Log.v(TAG, "setDecoder called: " + format.toString());


        fromFileToCodec = new FromFileToCodec(decoder, extractor);
        fromCodecToBuff = new FromCodecToBuff(decoder, buff);
        fromFileToCodec.setup(); // TODO forse e' meglio spostarlo sopra
    }

    public void start() throws InterruptedException
    {
        decoder.start();

        Thread t1 = new Thread(fromFileToCodec);
        Thread t2 = new Thread(fromCodecToBuff);
        t1.start();
        t2.start();
    }

    public MediaFormat getOutputFormat()
    {
        while (!fromCodecToBuff.isOutputFormatReady())
        {
        }
        return fromCodecToBuff.getOutputFormat();
    }

    public int getSampleRate()
    {
        return fromFileToCodec.getSampleRate();
    }
}
