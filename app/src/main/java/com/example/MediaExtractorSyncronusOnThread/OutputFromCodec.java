package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class OutputFromCodec implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private MediaCodec decoder;

    OutputFromCodec(MediaCodec decoder)
    {
        this.decoder = decoder;
    }

    @Override
    public void run()
    {
        int print = 0;
        boolean flag = true;
        while (flag)
        {
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            int outputBufferId = decoder.dequeueOutputBuffer(info, 3000);

            if (outputBufferId >= 0)
            {
                ByteBuffer outputBuffer = decoder.getOutputBuffer(outputBufferId);
                MediaFormat bufferFormat = decoder.getOutputFormat(outputBufferId); // option A

                Log.v(TAG, "-------------------------------- option A");

                if (outputBuffer != null)
                {
                    int cont = 0;
                    List<Byte> dati = new ArrayList<>();
                    while (outputBuffer.hasRemaining())
                    {
                        int pos = outputBuffer.position();
                        byte data = outputBuffer.get();

                        dati.add(data);

                        cont++;
                    }

                    if (print < 10)
                    {
                        Log.v(TAG, "cont -> " + cont + " -> data -> " + dati.toString());
                    }
                    print++;

                    Log.v(TAG, "presentationTimeUs: " + (info.presentationTimeUs + info.size));
                }
                else
                {
                    Log.v(TAG, "outputBuffer: null");
                }

                // bufferFormat is identical to outputFormat
                // outputBuffer is ready to be processed or rendered.
                decoder.releaseOutputBuffer(outputBufferId, 0);
            }
            else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED)
            {

                Log.v(TAG, "-------------------------------- option B");
                // Subsequent data will conform to new format.
                // Can ignore if using getOutputFormat(outputBufferId)
                MediaFormat outputFormat = decoder.getOutputFormat(); // option B

                Log.v(TAG, "outputFormat: " + outputFormat.toString());
            }
            else
            {
                Log.v(TAG, "-------------------------------- ne A ne B \n outputBufferId: " + (
                        outputBufferId == MediaCodec.INFO_TRY_AGAIN_LATER ? "INFO_TRY_AGAIN_LATER" : outputBufferId));
            }

            if (info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM)
            {
                flag = false;
            }
        }
    }
}
