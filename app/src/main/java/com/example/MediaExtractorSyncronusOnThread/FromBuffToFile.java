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


    /**
     * @param path   the path where the file will be saved
     * @param format the format of the output file
     *
     * @throws IOException if the path is invalid
     */
    FromBuffToFile(String path, @Format int format) throws IOException
    {
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


}
