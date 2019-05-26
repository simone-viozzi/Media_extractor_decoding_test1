package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClass;

import java.io.IOException;

public class Setup
{
    private final String TAG = getClass().getSimpleName();

    private String path;
    private ByteArrayTransferClass buff;


    public Setup(String path, ByteArrayTransferClass buff)
    {
        this.path = path;

        //this.path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/test_audio_file/audio2.m4a";
        //Log.v(TAG, "path: " + path);
        this.buff = buff;
    }

    public void start() throws IOException, InterruptedException
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

        MediaCodec decoder = MediaCodec.createDecoderByType(mAudioKeyMine); // non so se mAudioKeyMine va li'
        decoder.configure(format, null, null, 0);

        Log.v(TAG, "setDecoder called: " + format.toString());


        InputFromFile inputFromFile = new InputFromFile(decoder, extractor);
        OutputFromCodec outputFromCodec = new OutputFromCodec(decoder, buff);
        inputFromFile.setup(); // TODO forse e' meglio spostarlo sopra
        decoder.start();

        Thread t1 = new Thread(inputFromFile);
        Thread t2 = new Thread(outputFromCodec);
        t1.start();
        t2.start();


        t1.join();
        t2.join();
    }


}
