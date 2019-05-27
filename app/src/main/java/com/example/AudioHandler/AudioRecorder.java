package com.example.AudioHandler;

import android.media.MediaRecorder;

import java.io.IOException;

public class AudioRecorder
{


    private MediaRecorder recorder;

    public AudioRecorder()
    {
        recorder = new MediaRecorder();
    }

    public void configureRecording(String path)
    {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(path);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);
        recorder.setAudioSamplingRate(44100);
    }


    public void startRecording() throws IOException
    {
        recorder.prepare();
        recorder.start();
    }

    public void pauseRecording()
    {
        // on api level 23 there isn't a way to pause a recording.
        // the only way to do so is to stop the recording and create the file,
        // then reopen another file to continue the recording,
        // then join all the file
        // recorder.pause();
    }

    public void stopRecording()
    {
        recorder.stop();
        recorder.reset();
    }

}
