package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaCodec;
import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;

import java.nio.ByteBuffer;

public class FromBuffToCodec implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private MediaCodec decoder;
    private ByteArrayTransferClassV2 buff;

    FromBuffToCodec(MediaCodec decoder, ByteArrayTransferClassV2 buff)
    {
        this.decoder = decoder;
        this.buff = buff;
    }


    @Override
    public void run()
    {
        ByteBuffer inputBuffer;
        int inIndex;

        int cont = 0;

        boolean flag = true;
        while (flag)
        {
            // pijo l'indice
            inIndex = decoder.dequeueInputBuffer(1000);

            Log.d(TAG, "1 ok");

            if (inIndex >= 0)
            {
                if (!buff.isWorkDone())
                {
                    inputBuffer = decoder.getInputBuffer(inIndex);

                    //Log.d(TAG, "2 ok");

                    // uso il buffer appena preso per mettere i dati
                    if (inputBuffer != null)
                    {
                        while (!buff.isOkToDequeue())
                        {
                        }
                        byte[] b = buff.dequeue();
                        inputBuffer.put(b);

                        //Log.d(TAG, "3 ok");

                        int presentationTimeUs = 44100 * 16 * 1 * 10 ^ (-6) * (b.length * cont++);

                        decoder.queueInputBuffer(inIndex, 0, b.length, presentationTimeUs, 0);

                        //Log.d(TAG, "4 ok");
                    }
                }
                else
                {
                    decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    flag = false;
                }
            }
        }

    }
}
