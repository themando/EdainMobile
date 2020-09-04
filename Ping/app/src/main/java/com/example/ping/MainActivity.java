package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Thread Policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // init Python
        if (!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        // init GUI
        Button pingButton = (Button) findViewById(R.id.pingPageButton);
        Button speedButton = (Button) findViewById(R.id.speedPageButton);
        Button loadButton = (Button) findViewById(R.id.loadPageButton);
        final Button dnsResolutionButton = (Button) findViewById(R.id.dnsResolutionButton);

        // Ping page
        pingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Ping.class);
                startActivity(startIntent);
            }
        });

        // Speed Test page
        speedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Speed.class);
                startActivity(startIntent);
            }
        });

        // DNS Resolution Test Page
        dnsResolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), FragmentHolder.class);
                startActivity(startIntent);
            }
        });
        // Page Load Test page
        loadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Page.class);
                startActivity(startIntent);
            }
        });
    }

}
