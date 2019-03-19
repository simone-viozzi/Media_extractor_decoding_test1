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
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout Ll = findViewById(R.id.Ll);

        TextView t = new TextView(this);
        t.setText("boot ok");
        Ll.addView(t);

        ask_permission();

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
        LinearLayout Ll = findViewById(R.id.Ll);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED))
        {
            TextView t = new TextView(this);
            t.setText("asking permissions");
            Ll.addView(t);

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 110);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);



            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
        else
        {
            TextView t = new TextView(this);
            t.setText("permission aleready ok");
            Ll.addView(t);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        LinearLayout Ll = findViewById(R.id.Ll);

        TextView t = new TextView(this);
        t.setText("handler ok");
        Ll.addView(t);

        switch (requestCode)
        {
            case 110:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    t = new TextView(this);
                    t.setText("permission ok");
                    Ll.addView(t);
                }
                else
                {
                    t = new TextView(this);
                    t.setText("permission not granted");
                    Ll.addView(t);
                }
                return;
            }
            case 111:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    t = new TextView(this);
                    t.setText("permission ok");
                    Ll.addView(t);
                }
                else
                {
                    t = new TextView(this);
                    t.setText("permission not granted");
                    Ll.addView(t);
                }
                return;
            }

        }
    }


}
