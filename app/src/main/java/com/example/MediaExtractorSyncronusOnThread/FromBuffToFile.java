package com.example.MediaExtractorSyncronusOnThread;


import android.media.MediaMuxer;

import java.io.IOException;

/*
 * for this i must use MediaMuxer: form PCM -> mp4
 *
 * se voglio salvare il file in wav devo scrivermi l'heder a mano.
 * 44 cazzo di byte.
 *
 * */
public class FromBuffToFile //TODO !!!
{
    private MediaMuxer muxer;


    FromBuffToFile(String path, int format) throws IOException
    {
        muxer = new MediaMuxer(path, format);

    }
}
