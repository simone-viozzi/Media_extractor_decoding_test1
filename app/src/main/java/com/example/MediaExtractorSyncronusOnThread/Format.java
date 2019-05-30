package com.example.MediaExtractorSyncronusOnThread;

import android.media.MediaMuxer;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef({MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4,
         MediaMuxer.OutputFormat.MUXER_OUTPUT_WEBM,
         MediaMuxer.OutputFormat.MUXER_OUTPUT_3GPP,
         MediaMuxer.OutputFormat.MUXER_OUTPUT_HEIF,})
@interface Format
{
}
