package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.File;

public class Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        // init GUI
        Button loadButton = (Button) findViewById(R.id.loadButton);
        Button exportButton = (Button) findViewById(R.id.exportLoadButton);
        Button clearButton = (Button) findViewById(R.id.clearLoadButton);
        final EditText timeoutText = (EditText) findViewById(R.id.timeoutEditText);
        final EditText numUrlsText = (EditText) findViewById(R.id.numUrlsEditText);
        final EditText countyText = (EditText) findViewById(R.id.countyEditText);
        final EditText locationText = (EditText) findViewById(R.id.locationEditText);
        final EditText providerText = (EditText) findViewById(R.id.providerEditText);

        //final String fileString = this.getApplicationInfo().dataDir + File.separatorChar + "chromedriver";

        // Click Load Button
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int timeoutInt;
                int numUrlsInt;
                String timeoutStr = timeoutText.getText().toString();
                String numUrlsStr = numUrlsText.getText().toString();

                // Invalid inputs - empty
                if (timeoutStr.isEmpty() || numUrlsStr.isEmpty()) {
                    return;
                } else {
                    timeoutInt = Integer.parseInt(timeoutStr);
                    numUrlsInt = Integer.parseInt(numUrlsStr);
                }

                // Invalid inputs - values
                if (timeoutInt <= 0 || numUrlsInt <= 0) {
                    return;
                }

                String county = countyText.getText().toString();
                String location = locationText.getText().toString();
                String provider = providerText.getText().toString();
                String utc = "utc";

                // Run page load test
                Python py = Python.getInstance();
                PyObject pyf = py.getModule("load_page");
                PyObject obj = pyf.callAttr("main", timeoutInt, numUrlsInt, utc, county, location, provider, 1);
            }

        });


    }
}
