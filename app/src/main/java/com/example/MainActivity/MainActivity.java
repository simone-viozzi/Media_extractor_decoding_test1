package com.example.MainActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.AudioHandler.AudioPlayerFromFile;
import com.example.AudioHandler.AudioPlayerFromPCM;
import com.example.AudioHandler.AudioRecorder;
import com.example.ConsumerProducerV2.Setup2;
import com.example.ConsumerProducerV3.Setup;
import com.example.DataClasses.ByteArrayPrinterClass;
import com.example.DataClasses.ByteArrayTransferClassV2;
import com.example.MediaExtractorAsynchronous.MediaCodecAsyncTest;
import com.example.MediaExtractorSyncronusOnThread.SetupGetFromBuffToFile;
import com.example.MediaExtractorSyncronusOnThread.SetupGetFromFileToBuff;
import com.example.media_extractor_test1.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{


    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "boot ok");

        ask_permission();

        String mainPath = getExternalCacheDir().getAbsolutePath();
        String path1 = mainPath + "/audiorecordtest.mp4";
        Log.d(TAG, path1);

        AudioRecorder r = new AudioRecorder();

        Button startRecording = findViewById(R.id.recordingStart);
        startRecording.setOnClickListener(new startRecordingClick(r, path1));

        Button stopRecording = findViewById(R.id.recordingStop);
        stopRecording.setOnClickListener(new stopRecordingClick(r));


        AudioPlayerFromFile p = null;
        try
        {
            p = new AudioPlayerFromFile(path1);
        }
        catch (IOException e)
        {
            Log.e(TAG, "path non trovato");
            e.printStackTrace();
            return;
        }

        Button startPlayer = findViewById(R.id.playerStart);
        startPlayer.setOnClickListener(new startPlayerClick(p));

        Button stopPlayer = findViewById(R.id.playerStop);
        stopPlayer.setOnClickListener(new stopPlayerClick(p));

        Button startPlayerFromPCM = findViewById(R.id.playerStartFromPCM);
        startPlayerFromPCM.setOnClickListener(new startPlayerFromPCMClick(path1));


        Button startConsProd = findViewById(R.id.ProdCons_btn);
        startConsProd.setOnClickListener(new startConsProdClick());

        Button startConsProdV2 = findViewById(R.id.ProdConsV3_btn);
        startConsProdV2.setOnClickListener(new startConsProdV2Click());

        Button startConsProdV4 = findViewById(R.id.ProdConsV4_btn);
        startConsProdV4.setOnClickListener(new startConsProdV4Click());

        Button decodeV1 = findViewById(R.id.decode_v1);
        decodeV1.setOnClickListener(new decodeV1Click());

        Button decodeV2 = findViewById(R.id.decode_v2);
        decodeV2.setOnClickListener(new decodeV2Click(path1));

        Button decodeV3 = findViewById(R.id.decode_v3);
        decodeV3.setOnClickListener(new decodeV3Click(path1));

        Button decodeRecodeV1 = findViewById(R.id.decodeRecodeV1);
        decodeRecodeV1.setOnClickListener(new decodeRecodeV1Click(path1, mainPath + "/recodedaudio"));
    }

    private class decodeRecodeV1Click implements View.OnClickListener
    {
        private String path1;
        private String path2;

        public decodeRecodeV1Click(
                String path1, String path2)
        {

            this.path1 = path1;
            this.path2 = path2;
        }

        @Override
        public void onClick(View v)
        {
            ByteArrayTransferClassV2 buff = new ByteArrayTransferClassV2(64);

            SetupGetFromFileToBuff decoder = new SetupGetFromFileToBuff(path1, buff);
            try
            {
                decoder.configure();

                SetupGetFromBuffToFile encoder = new SetupGetFromBuffToFile(path2, buff);

                decoder.start();

                MediaFormat outputFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 22050, 1);
                Log.w(TAG, "mediaFormatnew = " + outputFormat.toString());

                encoder.configure(MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4, outputFormat);

                encoder.start();

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private class startPlayerFromPCMClick implements View.OnClickListener
    {
        private String path;

        startPlayerFromPCMClick(String path)
        {
            this.path = path;
        }

        @Override
        public void onClick(View v)
        {
            ByteArrayTransferClassV2 buff = new ByteArrayTransferClassV2(64);

            SetupGetFromFileToBuff decoder = new SetupGetFromFileToBuff(path, buff);
            try
            {
                decoder.configure();
                AudioPlayerFromPCM player = new AudioPlayerFromPCM(decoder.getSampleRate(), buff);

                decoder.start();

                player.startPlaying();
            }
            catch (IOException e)
            {
                Log.e(TAG, "errore nel file");
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "errore nei thread");
                e.printStackTrace();
            }
        }
    }


    private class startConsProdV4Click implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            com.example.ConsumerProducerV4.Setup s = new com.example.ConsumerProducerV4.Setup();
            try
            {
                s.start();
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "errore nei thread");
                e.printStackTrace();
            }
        }
    }


    private class startConsProdV2Click implements View.OnClickListener
    {
        startConsProdV2Click()
        {

        }

        @Override
        public void onClick(View v)
        {
            Setup s = new Setup();
            try
            {
                for (int i = 32; i <= 1024; i = i * 2)
                {
                    Log.d(TAG, "i = " + i);
                    s.start(i);
                }
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "error is some thread");
                e.printStackTrace();
            }
        }
    }


    private class decodeV3Click implements View.OnClickListener
    {
        private String path;

        decodeV3Click(String path)
        {
            this.path = path;
        }

        @Override
        public void onClick(View v)
        {
            ByteArrayTransferClassV2 buff = new ByteArrayTransferClassV2(512);

            SetupGetFromFileToBuff setupGetFromFileToBuff = new SetupGetFromFileToBuff(path, buff);
            try
            {
                setupGetFromFileToBuff.configure();
            }
            catch (IOException e)
            {
                Log.e(TAG, "errore nel file");
                e.printStackTrace();
            }

            ByteArrayPrinterClass printerClass = new ByteArrayPrinterClass(buff);
            Thread tP = new Thread(printerClass);

            try
            {
                tP.start();
                setupGetFromFileToBuff.start();
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "errore nei thread");
                e.printStackTrace();
            }
        }
    }


    class decodeV2Click implements View.OnClickListener
    {
        private String path;

        decodeV2Click(String path)
        {
            this.path = path;
        }

        @Override
        public void onClick(View v)
        {
            MediaCodecAsyncTest m;
            try
            {
                m = new MediaCodecAsyncTest(path);

            }
            catch (IOException e)
            {
                Log.v(TAG, "MediaCodecAsyncTest creation error");
                e.printStackTrace();
                return;
            }
            m.decode();
        }
    }

    class decodeV1Click implements View.OnClickListener
    {
        decodeV1Click()
        {

        }

        @Override
        public void onClick(View v)
        {
            media_extractor_test m = new media_extractor_test();

            MediaExtractor extractor = m.getHeader();

            m.getData(extractor);

            Log.v(TAG, "ho finito e non sono scoppiato");
        }
    }

    class startConsProdClick implements View.OnClickListener
    {
        startConsProdClick()
        {

        }

        @Override
        public void onClick(View v)
        {
            Setup2 s = new Setup2();
            s.start(30);
        }
    }

    class startPlayerClick implements View.OnClickListener
    {
        AudioPlayerFromFile p;

        startPlayerClick(AudioPlayerFromFile p)
        {
            this.p = p;
        }

        @Override
        public void onClick(View v)
        {
            Log.v(TAG, "starting playing");
            p.startPlaying();
            Log.v(TAG, "playing started");
        }
    }

    class stopPlayerClick implements View.OnClickListener
    {
        AudioPlayerFromFile p;

        stopPlayerClick(AudioPlayerFromFile p)
        {
            this.p = p;
        }

        @Override
        public void onClick(View v)
        {
            p.stopPlaying();
        }
    }

    private class startRecordingClick implements View.OnClickListener
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

    private class stopRecordingClick implements View.OnClickListener
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


    private void ask_permission()
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
