package com.example.media_extractor_test1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaExtractor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.AudioHandler.AudioPlayer;
import com.example.AudioHandler.AudioRecorder;
import com.example.ConsumerProducer.Setup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "boot ok");

        ask_permission();

        String path = getExternalCacheDir().getAbsolutePath();
        path += "/audiorecordtest.mp4";
        Log.d(TAG, path);

        AudioRecorder r = new AudioRecorder();

        Button startRecording = findViewById(R.id.recordingStart);
        startRecording.setOnClickListener(new startRecordingClick(r, path));

        Button stopRecording = findViewById(R.id.recordingStop);
        stopRecording.setOnClickListener(new stopRecordingClick(r));


        AudioPlayer p = new AudioPlayer();

        Button startPlayer = findViewById(R.id.playerStart);
        startPlayer.setOnClickListener(new startPlayerClick(p, path));

        Button stopPlayer = findViewById(R.id.playerStop);
        stopPlayer.setOnClickListener(new stopPlayerClick(p));


        Button startConsProd = findViewById(R.id.ProdCons_btn);
        startConsProd.setOnClickListener(new startConsProdClick());

    }


    class startConsProdClick implements View.OnClickListener
    {
        startConsProdClick()
        {

        }

        @Override
        public void onClick(View v)
        {
            Setup s = new Setup();
            s.start();
        }
    }

    class startPlayerClick implements View.OnClickListener
    {
        AudioPlayer p;
        String path;

        startPlayerClick(AudioPlayer p, String path)
        {
            this.p = p;
            this.path = path;
        }

        @Override
        public void onClick(View v)
        {
            Log.v(TAG, "starting playing");
            try
            {
                p.startPlaying(path);
            }
            catch (IOException e)
            {
                Log.v(TAG, "falied to prepare");
                return;
            }
            Log.v(TAG, "playing started");
        }
    }

    class stopPlayerClick implements View.OnClickListener
    {
        AudioPlayer p;

        stopPlayerClick(AudioPlayer p)
        {
            this.p = p;
        }

        @Override
        public void onClick(View v)
        {
            p.stopPlaying();
        }
    }

    class startRecordingClick implements View.OnClickListener
    {
        AudioRecorder r;
        String path;

        startRecordingClick(AudioRecorder r, String path)
        {
            this.r = r;
            this.path = path;
        }

        @Override
        public void onClick(View v)
        {
            Log.v(TAG, "starting recording");
            r.configureRecording(path);

            try
            {
                r.startRecording();
            }
            catch (IOException e)
            {
                Log.v(TAG, "falied to prepare");
                return;
            }
            Log.v(TAG, "recording started");
        }
    }

    class stopRecordingClick implements View.OnClickListener
    {
        AudioRecorder r;

        stopRecordingClick(AudioRecorder r)
        {
            this.r = r;
        }

        @Override
        public void onClick(View v)
        {
            Log.v(TAG, "stopping recording");
            r.stopRecording();
            Log.v(TAG, "recording stopped");
        }
    }

    public void onClickHandler1(View v)
    {
        media_extractor_test m = new media_extractor_test();

        MediaExtractor extractor = m.getHeader();

        m.getData(extractor);

        Log.v(TAG, "ho finito e non sono scoppiato");
    }

    public void ask_permission()
    {

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED))
        {

            Log.v(TAG, "asking permissions");

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 110);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 112);


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
        else
        {

            Log.v(TAG, "permission aleready ok");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        Log.v(TAG, "handler ok");

        switch (requestCode)
        {
            case 110:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    Log.v(TAG, "permission ok");

                }
                else
                {

                    Log.v(TAG, "permission not granted");
                }
                return;
            }
            case 111:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    Log.v(TAG, "permission ok");

                }
                else
                {

                    Log.v(TAG, "permission not granted");

                }
                return;
            }
            case 112:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    Log.v(TAG, "permission ok");

                }
                else
                {

                    Log.v(TAG, "permission not granted");

                }
                return;
            }

        }
    }


}
