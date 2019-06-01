package com.example.MediaExtractorSyncronusOnThread;


import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import com.example.DataClasses.ByteArrayTransferClassV2;

import java.io.IOException;
import java.nio.ByteBuffer;

/*
 * for this i must use MediaMuxer: from mp4 to mp4
 *
 * se voglio salvare il file in wav devo scrivermi l'heder a mano.
 * 44 cazzo di byte.
 *
 * */
public class FromBuffToFile implements Runnable
{
    private final String TAG = getClass().getSimpleName();


    /*
     * media muxer can mux stream of encoded data and save it
     * */
    private MediaMuxer muxer;
    private MediaCodec.BufferInfo bufferInfo;
    private ByteArrayTransferClassV2 buff;
    private int audioTrackIndex;


    /**
     * @param path   the path where the file will be saved
     * @param format the format of the output file
     *
     * @throws IOException if the path is invalid
     */
    FromBuffToFile(String path, @Format int format, ByteArrayTransferClassV2 buff)
            throws IOException
    {
        this.buff = buff;
        String ext = "";
        switch (format)
        {
            case MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4:
                ext = ".mp4";
                break;
            case MediaMuxer.OutputFormat.MUXER_OUTPUT_WEBM:
                ext = ".webm";
                break;
            case MediaMuxer.OutputFormat.MUXER_OUTPUT_3GPP:
                ext = ".3gp";
                break;
            case MediaMuxer.OutputFormat.MUXER_OUTPUT_HEIF:
                ext = ".heif";
                break;
        }
        muxer = new MediaMuxer(path + ext, format);
    }

    void addTrack(MediaFormat mediaFormat)
    {
        audioTrackIndex = muxer.addTrack(mediaFormat);
    }

    private void start()
    {
        muxer.start();
        bufferInfo = new MediaCodec.BufferInfo();
    }

    private void write(ByteBuffer b)
    {
        muxer.writeSampleData(audioTrackIndex, b, bufferInfo);
    }

    private void stop()
    {
        muxer.stop();
        muxer.release();
    }

    @Override
    public void run()
    {
        Log.d(TAG, "encoding started");
        int i = 0;
        start();
        while (!buff.isWorkDone())
        {
            while (!buff.isOkToDequeue())
            {
            }
            byte[] b = buff.dequeue();
            ByteBuffer buffer = ByteBuffer.allocate(b.length);
            buffer.put(b);
            write(buffer);
            buffer.clear();
            Log.d(TAG, "wrote " + i++ + " buffers");
        }
        stop();
        Log.d(TAG, "encoding stopped");
    }
}
