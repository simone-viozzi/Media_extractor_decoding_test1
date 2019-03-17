package com.example.media_extractor_test1;

import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
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

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/test_audio_file/audio.m4a";
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
            decoder = MediaCodec.createDecoderByType(mAudioKeyMine); // non so se mAudioKeyMine va li`
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

        int sample = 0;
        int offset = 0;

        int print = 0;

        ByteBuffer inputBuffer = ByteBuffer.allocate(2048);

        MediaFormat outputFormat = decoder.getOutputFormat();
        Log.v(TAG, "outputFormat: " + outputFormat.toString());

        decoder.start();

        int index = decoder.dequeueInputBuffer(1000);

        boolean sawInputEOS = false;

        // extractor.readSampleData -> Retrieve the current encoded sample
        // and store it in the byte buffer starting at the given offset.
        while ((sample >= 0) && (print < 10))
        {

            int inputBufferId = decoder.dequeueInputBuffer(1000);
            if (inputBufferId >= 0)
            {
                inputBuffer = decoder.getInputBuffer(index);

                sample = extractor.readSampleData(inputBuffer, 0);

                long presentationTimeUs = 0;

                if (sample < 0)
                {
                    sawInputEOS = true;
                    sample = 0;
                }
                else
                {

                    int trackIndex = extractor.getSampleTrackIndex();
                    presentationTimeUs = extractor.getSampleTime();

                    Log.v(TAG, "trackIndex: " + trackIndex + ", presentationTimeUs: " + presentationTimeUs);
                    Log.v(TAG, "sample: " + sample + ", offset: " + offset);
                    Log.v(TAG, "inputBuffer: " + inputBuffer.toString());

                }

                decoder.queueInputBuffer(inputBufferId, 0, sample, presentationTimeUs, sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);

                if (!sawInputEOS)
                {
                    extractor.advance();
                }

            }


            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            Log.v(TAG, "info: " + info.toString());

            int outputBufferId = decoder.dequeueOutputBuffer(info, 1000);

            Log.v(TAG, "info: " + info.toString());

            if (outputBufferId >= 0)
            {
                ByteBuffer outputBuffer = decoder.getOutputBuffer(outputBufferId);
                MediaFormat bufferFormat = decoder.getOutputFormat(outputBufferId); // option A

                Log.v(TAG, "option A");
                Log.v(TAG, "outputBufferId: " + outputBufferId);
                if (outputBuffer != null)
                {
                    Log.v(TAG, "outputBuffer: " + outputBuffer.toString());
                }
                else
                {
                    Log.v(TAG, "outputBuffer: null");
                }
                Log.v(TAG, "bufferFormat: " + bufferFormat.toString());


                int cont = 0;
                if (outputBuffer != null)
                {
                    while (outputBuffer.hasRemaining())
                    {
                        int pos = outputBuffer.position();
                        byte data = outputBuffer.get();

                        if (cont < 10)
                        {
                            Log.v(TAG, "outputBuffer: " + pos + " -> " + data);
                        }
                        cont++;
                    }
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

                Log.v(TAG, "option B");
                // Subsequent data will conform to new format.
                // Can ignore if using getOutputFormat(outputBufferId)
                outputFormat = decoder.getOutputFormat(); // option B

                Log.v(TAG, "outputFormat: " + outputFormat.toString());
            }

            Log.v(TAG, "extractor.advance()");

            offset += sample;

            print++;
        }

        Log.v(TAG, "end of track");

        extractor.release();
        extractor = null;
        decoder.stop();
        decoder.release();

    }

}


