package com.example.MediaExtractorAsynchronous;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class MediaCodecAsyncTest
{
    private final String TAG = getClass().getSimpleName();

    private MediaCodec codec;
    private MediaFormat mOutputFormat; // member variable
    private MediaFormat mInputFormat;
    private MediaExtractor extractor;


    public MediaCodecAsyncTest(String path) throws IOException
    {
        String mAudioKeyMine;

        extractor = new MediaExtractor();

        extractor.setDataSource(path);
        Log.v(TAG, "setted path");


        mInputFormat = extractor.getTrackFormat(0);
        mAudioKeyMine = mInputFormat.getString(MediaFormat.KEY_MIME);
        Log.v(TAG, "format: " + mInputFormat + ", mime: " + mAudioKeyMine);

        extractor.selectTrack(0);

        codec = MediaCodec.createDecoderByType(mAudioKeyMine); // non so se mAudioKeyMine va li'

    }

    public void decode()
    {


        codec.setCallback(new MediaCodec.Callback()
        {
            @Override
            public void onInputBufferAvailable(MediaCodec mc, int inputBufferId)
            {
                ByteBuffer inputBuffer = codec.getInputBuffer(inputBufferId);
                // fill inputBuffer with valid data

                int sampleSize = extractor.readSampleData(inputBuffer, 0);

                if (sampleSize < 0)
                {
                    Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());
                    Log.d(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");


                    codec.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                }
                else
                {
                    Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());

                    //extractor.getSampleTime();

                    codec.queueInputBuffer(inputBufferId, 0, sampleSize, extractor.getSampleTime(), 0);
                    extractor.advance();

                    Log.v(TAG, "extractor.advance()");


                }
            }

            @Override
            public void onOutputBufferAvailable(MediaCodec mc, int index,
                                                MediaCodec.BufferInfo info)
            {
                int outputBufferId = codec.dequeueOutputBuffer(info, 3000);

                ByteBuffer outputBuffer = codec.getOutputBuffer(outputBufferId);
                MediaFormat bufferFormat = codec.getOutputFormat(outputBufferId); // option A
                // bufferFormat is equivalent to mOutputFormat
                // outputBuffer is ready to be processed or rendered.

                int print = 0;

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
                }
                else
                {
                    Log.v(TAG, "outputBuffer: null");
                }


                codec.releaseOutputBuffer(outputBufferId, 0);
            }

            @Override
            public void onOutputFormatChanged(MediaCodec mc, MediaFormat format)
            {
                // Subsequent data will conform to new format.
                // Can ignore if using getOutputFormat(outputBufferId)
                mOutputFormat = format; // option B
            }

            @Override
            public void onError(MediaCodec codec,
                                MediaCodec.CodecException e)
            {

            }
        });
        codec.configure(mInputFormat, null, null, 0);

        codec.start();
        Log.w(TAG, " decoding started");


        // wait for processing to complete
        codec.stop();
        Log.w(TAG, "decoding stopped");

        codec.release();
    }

}
