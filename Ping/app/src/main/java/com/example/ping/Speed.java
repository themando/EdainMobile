package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.File;

// Using Chaquopy: Python SDK for Android to execute run_speedtest.py
// https://chaquo.com/chaquopy/
//
// Running unlicensed version of Chaquopy:
// The unlicensed SDK is fully functional, but apps built with it will display
// a notification on startup, and will only run for 5 minutes at a time.
// To remove these restrictions, a license is required.
// https://chaquo.com/chaquopy/license/

public class Speed extends Activity {
    private static final String FILE_NAME = "speedtest.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        // init GUI
        Button speedButton = (Button) findViewById(R.id.speedButton);
        Button exportButton = (Button) findViewById(R.id.exportSpeedButton);
        Button clearButton = (Button) findViewById(R.id.clearSpeedButton);
        final EditText classText = (EditText) findViewById(R.id.classEditText);
        final EditText intervText = (EditText) findViewById(R.id.intervalEditText);

        // Click Speed Button
        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int classInt;
                int intervInt;
                String classStr = classText.getText().toString();
                String intervStr = intervText.getText().toString();

                // Invalid inputs - empty
                if (classStr.isEmpty() || intervStr.isEmpty()) {
                    return;
                } else {
                    classInt = Integer.parseInt(classStr);
                    intervInt = Integer.parseInt(intervStr);
                }

                // Invalid inputs - values
                if (classInt <= 0 || intervInt <= 0) {
                    return;
                }

                // Run speed test
                Python py = Python.getInstance();
                PyObject pyf = py.getModule("run_speedtest");
                PyObject obj = pyf.callAttr("main", classInt, intervInt);
            }
        });

        // Click Clear Button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCsv();
            }
        });

        // Click Export Button
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });
    }

    // Remove csv file
    public void deleteCsv() {
        boolean res = deleteFile(FILE_NAME);
        Toast.makeText(this, "speedtest.csv cleared", Toast.LENGTH_LONG).show();
    }

    // Export csv file
    public void export() {

        try {
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), FILE_NAME);
            Uri path = FileProvider.getUriForFile(context, "com.example.Ping.fileprovider", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Speedtest Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Export speedtest.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
