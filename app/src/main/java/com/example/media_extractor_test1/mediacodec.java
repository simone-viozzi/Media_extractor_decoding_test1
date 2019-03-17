package com.example.media_extractor_test1;

import android.media.MediaCodec;
import android.media.MediaFormat;

import java.nio.ByteBuffer;

/*
public class mediacodec
{
    MediaCodec codec = MediaCodec.createByCodecName(name);
    codec.configure(format, …);
    MediaFormat outputFormat = codec.getOutputFormat(); // option B
    codec.start();
    for(;;)
    {

        int inputBufferId = codec.dequeueInputBuffer(timeoutUs);
        if (inputBufferId >= 0)
        {
            ByteBuffer inputBuffer = codec.getInputBuffer(…);
            // fill inputBuffer with valid data
            codec.queueInputBuffer(inputBufferId, …);
        }


        int outputBufferId = codec.dequeueOutputBuffer(…);

        if (outputBufferId >= 0)
        {
            ByteBuffer outputBuffer = codec.getOutputBuffer(outputBufferId);
            MediaFormat bufferFormat = codec.getOutputFormat(outputBufferId); // option A
            // bufferFormat is identical to outputFormat
            // outputBuffer is ready to be processed or rendered.
            codec.releaseOutputBuffer(outputBufferId, …);
        }
        else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED)
        {
            // Subsequent data will conform to new format.
            // Can ignore if using getOutputFormat(outputBufferId)
            outputFormat = codec.getOutputFormat(); // option B
        }
    }
    codec.stop();
    codec.release();

}
*/
