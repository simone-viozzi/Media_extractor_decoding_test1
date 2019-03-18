package com.example.media_extractor_test1;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class media_extractor_test
{
    protected final String TAG = getClass().getSimpleName();

    private int mAudioChannels;
    private int mAudioSampleRate;
    private int mAudioBitRate;
    private int mAudioDurationUs;
    private String mAudioKeyMine;

    MediaCodec decoder;

    media_extractor_test()
    {

    }


    public MediaExtractor getHeader()
    {
        Log.v(TAG, "media_exctractor_test called");

        try
        {
            MediaExtractor extractor = new MediaExtractor();

            /// percorso dell'audio
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/test_audio_file/audio2.m4a";
            Log.v(TAG, "path: " + path);

            extractor.setDataSource(path);
            Log.v(TAG, "setted path");

            int numTracks = extractor.getTrackCount();
            Log.v(TAG, "numTracks: " + numTracks);

            MediaFormat format = extractor.getTrackFormat(0);
            mAudioKeyMine = format.getString(MediaFormat.KEY_MIME);
            Log.v(TAG, "format: " + format + ", mime: " + mAudioKeyMine);

            extractor.selectTrack(0);

            mAudioChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            mAudioSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

            ////// need ittttt
            mAudioDurationUs = format.getInteger(MediaFormat.KEY_DURATION);

            //mAudioBitRate = format.getInteger(MediaFormat.KEY_BIT_RATE);

            setDecoder(format);

            return extractor;

        }
        catch (IOException e)
        {
            Log.v(TAG, "----------------- errorrrrrr -----------------------------");
            e.printStackTrace();

            return null;
        }
    }


    private boolean setDecoder(MediaFormat format)
    {
        try
        {
            decoder = MediaCodec.createDecoderByType(mAudioKeyMine); // non so se mAudioKeyMine va li'
            decoder.configure(format, null, null, 0);

            Log.v(TAG, "setDecoder called: " + format.toString());

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void getData(MediaExtractor extractor)
    {
        Log.v(TAG, "buffer allocated");

        int sampleSize = 0;
        int offset = 0;

        int print = 0;

        ByteBuffer inputBuffer = ByteBuffer.allocate(2048);

        MediaFormat outputFormat = decoder.getOutputFormat();
        Log.v(TAG, "outputFormat: " + outputFormat.toString());

        decoder.start();

        //int index = decoder.dequeueInputBuffer(1000);

        //// il metodo non funziona TODO
        boolean EOS = false;
        long lastPresentationTimeUs = -1;

        // extractor.readSampleData -> Retrieve the current encoded sampleSize
        // and store it in the byte buffer starting at the given offset.

        // devo ciclare fino a !eosReceived
        //while (sampleSize >= 0)
        while (!EOS) // TODO
        {

            // pijo l'indice
            int inIndex = decoder.dequeueInputBuffer(1000);
            // se l'indice e' > 0 va bene (senno ???)
            if (inIndex >= 0)
            {
                // pijo il buffer di ingresso
                inputBuffer = decoder.getInputBuffer(inIndex);

                // uso il buffer appena preso per mettere i dati
                sampleSize = extractor.readSampleData(inputBuffer, 0);
                // ho estratto sampleSize dati

                long presentationTimeUs = 0;

                // se sampleSize < 0 ho raggiunto la fine del file
                if (sampleSize < 0)
                {
                    Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());
                    Log.d(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");
                    decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);

                }
                else
                {
                    Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());

                    lastPresentationTimeUs = extractor.getSampleTime();

                    decoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                    extractor.advance();

                    Log.v(TAG, "extractor.advance()");


                    //Log.v(TAG, "sampleSize: " + sampleSize + ", offset: " + offset);
                    //Log.v(TAG, "inputBuffer: " + inputBuffer.toString());

                }

            }


            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            int outputBufferId = decoder.dequeueOutputBuffer(info, 10000);

            Log.v(TAG, "info: info.flags -> " + info.flags +
                    ", info.offset -> " + info.offset +
                    ", info.presentationTimeUs -> " + info.presentationTimeUs +
                    ", info.size -> " + info.size);

            boolean flag = false; // TODO

            if (lastPresentationTimeUs == info.presentationTimeUs)
            {
                Log.v(TAG, "lastPresentationTimeUs = info.presentationTimeUs");
                flag = true;
            }


            if (outputBufferId >= 0)
            {
                ByteBuffer outputBuffer = decoder.getOutputBuffer(outputBufferId);
                MediaFormat bufferFormat = decoder.getOutputFormat(outputBufferId); // option A

                Log.v(TAG, "-------------------------------- option A");

                Log.v(TAG, "outputBufferId: " +
                        (outputBufferId==MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED ? "INFO_OUTPUT_BUFFERS_CHANGED" :
                                (outputBufferId==MediaCodec.INFO_OUTPUT_FORMAT_CHANGED ? "INFO_OUTPUT_FORMAT_CHANGED" :
                                (outputBufferId==MediaCodec.INFO_TRY_AGAIN_LATER ? "INFO_TRY_AGAIN_LATER" :
                                        outputBufferId))));



                if (outputBuffer != null)
                {
                    int cont = 0;
                    while (outputBuffer.hasRemaining())
                    {
                        int pos = outputBuffer.position();
                        byte data = outputBuffer.get();


                        if (cont < 3)
                        {
                            //Log.v(TAG, "data -> " + data);
                        }
                        cont++;
                    }
                    Log.v(TAG, "cont -> " + cont + " <-----");
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
                outputFormat = decoder.getOutputFormat(); // option B

                Log.v(TAG, "outputFormat: " + outputFormat.toString());
            }
            else
            {
                Log.v(TAG, "-------------------------------- ne A ne B \n outputBufferId: " + (outputBufferId==MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED ? "INFO_OUTPUT_BUFFERS_CHANGED" : (
                        outputBufferId==MediaCodec.INFO_OUTPUT_FORMAT_CHANGED ? "INFO_OUTPUT_FORMAT_CHANGED" : (
                                outputBufferId==MediaCodec.INFO_TRY_AGAIN_LATER ? "INFO_TRY_AGAIN_LATER" : outputBufferId))));
            }

            if (flag) // TODO
            {
                EOS = true;
            }

        }

        Log.v(TAG, "end of track");

        extractor.release();
        extractor = null;
        decoder.stop();
        decoder.release();

    }

}


