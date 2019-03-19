package com.example.media_extractor_test1;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class media_extractor_test
{
    protected final String TAG = getClass().getSimpleName();

    //private long mAudioDurationUs;
    private String mAudioKeyMine;

    private MediaCodec decoder;

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

            //mAudioChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            //mAudioSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

            //mAudioDurationUs = format.getLong(MediaFormat.KEY_DURATION);

            //mAudioBitRate = format.getInteger(MediaFormat.KEY_BIT_RATE);

            //Log.v(TAG, "mAudioDurationUs -> " + mAudioDurationUs);

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

        int sampleSize;
        int offset = 0;

        int print = 0;

        ByteBuffer inputBuffer;

        MediaFormat outputFormat = decoder.getOutputFormat();
        Log.v(TAG, "outputFormat: " + outputFormat.toString());

        decoder.start();

        boolean EOS = false;

        // extractor.readSampleData -> Retrieve the current encoded sampleSize
        // and store it in the byte buffer starting at the given offset.

        // devo ciclare fino a !eosReceived
        //while (sampleSize >= 0)
        while (!EOS)
        {

            long presentationTimeUs = 0;

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
                        Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());
                        Log.d(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");

                        presentationTimeUs = extractor.getSampleTime();

                        decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    }
                    else
                    {
                        Log.v(TAG, "presentationTimeUs: " + extractor.getSampleTime());

                        //extractor.getSampleTime();

                        decoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                        extractor.advance();

                        Log.v(TAG, "extractor.advance()");


                    }
                }

            }


            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            int outputBufferId = decoder.dequeueOutputBuffer(info, 3000);

            Log.v(TAG, "info: info.flags -> " + info.flags +
                    ", info.offset -> " + info.offset +
                    ", info.presentationTimeUs -> " + info.presentationTimeUs +
                    ", info.size -> " + info.size);

            boolean flag = false; // TODO

            if ((info.presentationTimeUs == 0) && (presentationTimeUs == -1))
            {
                Log.v(TAG, "lastPresentationTimeUs = info.presentationTimeUs");
                EOS = true;
            }

            //long duration


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
                Log.v(TAG, "-------------------------------- ne A ne B \n outputBufferId: " + (
                                outputBufferId==MediaCodec.INFO_TRY_AGAIN_LATER ? "INFO_TRY_AGAIN_LATER" : outputBufferId));
            }

        }

        Log.v(TAG, "end of track");

        extractor.release();
        decoder.stop();
        decoder.release();

    }

}


