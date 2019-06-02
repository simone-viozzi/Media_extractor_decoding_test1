package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.nio.ByteBuffer;


public class FromFileToCodec implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private MediaCodec decoder;
    private MediaExtractor extractor;
    private int sampleRate = 0;

    FromFileToCodec(MediaCodec decoder, MediaExtractor extractor)
    {
        this.decoder = decoder;
        this.extractor = extractor;
    }

    void setup() // TODO se lo fa nel costruttore funziona?
    {
        MediaFormat outputFormat = decoder.getOutputFormat();
        sampleRate = outputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        //Log.v(TAG, "outputFormat: " + outputFormat.toString());
    }

    @Override
    public void run()
    {
        int sampleSize;
        ByteBuffer inputBuffer;

        boolean flag = true;
        while (flag)
        {

            // pijo l'indice
            int inIndex = decoder.dequeueInputBuffer(1000);
            // se l'indice e' > 0 va bene (senno ???)
            if (inIndex >= 0)
            {
                // pijo il buffer di ingresso
                inputBuffer = decoder.getInputBuffer(inIndex);

                // uso il buffer appena preso per mettere i dati
                if (inputBuffer != null)
                {
                    sampleSize = extractor.readSampleData(inputBuffer, 0);

                    // ho estratto sampleSize dati

                    // se sampleSize < 0 ho raggiunto la fine del file
                    if (sampleSize < 0)
                    {
                        //Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());
                        //Log.d(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");

                        decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);

                        flag = false;
                    }
                    else
                    {
                        //Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());

                        //extractor.getSampleTime();

                        decoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                        extractor.advance();

                        //Log.v(TAG, "extractor.advance()");


                    }
                }

            }
        }
    }

    public int getSampleRate()
    {
        if (sampleRate == 0)
        {
            throw new IllegalStateException("prima chiama il setup!");
        }
        return sampleRate;
    }
}
