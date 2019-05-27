package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;

import java.nio.ByteBuffer;

public class FromCodecToBuff implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private MediaCodec decoder;
    private ByteArrayTransferClassV2 buff;

    FromCodecToBuff(MediaCodec decoder, ByteArrayTransferClassV2 buff)
    {
        this.decoder = decoder;

        this.buff = buff;
    }

    @Override
    public void run()
    {
        MediaCodec.BufferInfo info;
        do
        {
            info = new MediaCodec.BufferInfo();

            int outputBufferId = decoder.dequeueOutputBuffer(info, 3000);

            if (outputBufferId >= 0)
            {
                ByteBuffer outputBuffer = decoder.getOutputBuffer(outputBufferId);
                MediaFormat bufferFormat = decoder.getOutputFormat(outputBufferId); // option A

                //Log.v(TAG, "-------------------------------- option A");

                if (outputBuffer != null)
                {
                    //Log.e(TAG, "outputBuffer.hasArray() = " + outputBuffer.hasArray());
                    //Log.d(TAG, "outputBuffer.remaining() = " + outputBuffer.remaining());

                    byte[] b = new byte[outputBuffer.remaining()];
                    outputBuffer.get(b);
                    //Log.d(TAG, "data -> " + Arrays.toString(b));
                    while (!buff.isOkToEnqueue())
                    {
                    }
                    buff.enqueue(b);
                    //Log.w(TAG, "dati caricati");

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

                //Log.v(TAG, "-------------------------------- option B");
                // Subsequent data will conform to new format.
                // Can ignore if using getOutputFormat(outputBufferId)
                MediaFormat outputFormat = decoder.getOutputFormat(); // option B

                Log.v(TAG, "new outputFormat: " + outputFormat.toString());
            }
            else
            {
                // Log.v(TAG, "-------------------------------- ne A ne B \n outputBufferId: " + (
                //      outputBufferId == MediaCodec.INFO_TRY_AGAIN_LATER ? "INFO_TRY_AGAIN_LATER" : outputBufferId));
            }

        }
        while (info.flags != MediaCodec.BUFFER_FLAG_END_OF_STREAM);

        buff.setWorkDone(true);
    }
}
