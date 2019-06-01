package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;

import java.io.IOException;


public class SetupGetFromBuffToFile
{
    private final String TAG = getClass().getSimpleName();

    private String path;
    private ByteArrayTransferClassV2 buff1;
    private FromBuffToFile fromBuffToFile;
    private FromBuffToCodec fromBuffToCodec;
    private FromCodecToBuff fromCodecToBuff;
    private MediaCodec decoder;

    public SetupGetFromBuffToFile(String path, ByteArrayTransferClassV2 buff)
    {
        this.path = path;
        this.buff1 = buff;
    }

    public void configure(int format, MediaFormat outputFormat) throws IOException
    {
        decoder = MediaCodec.createDecoderByType(outputFormat.getString(MediaFormat.KEY_MIME));

        decoder.configure(outputFormat, null, null, 0);

        Log.v(TAG, "setDecoder called: " + outputFormat.toString());

        fromBuffToCodec = new FromBuffToCodec(decoder, buff1);

        ByteArrayTransferClassV2 buff2 = new ByteArrayTransferClassV2(64);

        fromCodecToBuff = new FromCodecToBuff(decoder, buff2);
        fromBuffToFile = new FromBuffToFile(path, format, buff2);
        fromBuffToFile.addTrack(outputFormat);
    }

    public void start()
    {
        decoder.start();
        Thread t1 = new Thread(fromBuffToCodec);
        Thread t2 = new Thread(fromCodecToBuff);
        Thread t3 = new Thread(fromBuffToFile);
        t1.start();
        t2.start();
        t3.start();
    }

}
