package com.example.ping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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
    Random random = new Random();
    int randomNumber = random.nextInt(999999999);
    int doc_ser = randomNumber;
    public JSONObject json;


    speedtest async = new speedtest();


    /**
     * Adding Firestore services:
     */ FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        // Warning toast, to alert user that this feature will take some time to complete
        Toast toast = Toast.makeText(Speed.this, "Please wait after clicking on Speed- this will take 3-4 minutes!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();


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
                    classInt = 1;
                    intervInt = 1;
                } else {
                    classInt = Integer.parseInt(classStr);
                    intervInt = Integer.parseInt(intervStr);

                }

                // Invalid inputs - values
                if (classInt <= 0 || intervInt <= 0) {
                    return;
                }

                // Run speed test
                Log.i("Speed.java","cli launch");
//                String cmd = "/system/bin/speedtest-cli "+ "--json";
//                Process p = executeCmd(cmd);
//                Log.i("CLI", String.valueOf(p));


                Python py = Python.getInstance();
                PyObject pyf = py.getModule("run_speedtest");
                pyf.callAttr("main", classInt, intervInt);
                PyObject obj = pyf.callAttr("speedtest");
                Log.i("printing obj", String.valueOf(obj));

                Map<String, Object> m = new HashMap<>();

             //Converting PyObject to String then json
                try {

                    Map<String, Object> map = new HashMap<>();
                   map = jsonToMap(String.valueOf(obj));
                   m.put("data",map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            //Saving results to Firestore

                Log.i("final map:", String.valueOf(m));

                db.collection("speedtest").document(String.valueOf(doc_ser)).set(m)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FIRESTORE", "Error writing document", e);
                            }
                        });
                Log.i("Finished","line 77");

                Toast.makeText(Speed.this, "All done!!", Toast.LENGTH_LONG).show();

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
    HashMap<String, Object> map = new HashMap<>();
    public Map<String, Object> jsonToMap(String t) throws JSONException {
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ) {
            String key = (String) keys.next();
            Object value = null;
            if (key.equals("server") || key.equals("client")) {

                map.put(key,jsonToMap(jObject.getString(key)));
                Log.i("map after client/server", String.valueOf(map));
            } else {
                try {
                    value = jObject.getString(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put(key, value);
                Log.i("normal map", String.valueOf(map));
            }
        }

        return map;
    }

    public static Process executeCmd(String cmd){
        Process p = null;
        try {
            Runtime.getRuntime().exec("pip install speedtest-cli");
            p = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("EXECUTE", "Error executing command.");
        }
        return p;
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

    public class speedtest extends AsyncTask<int[], String, Void>{
        @Override
        protected void onPreExecute(){
            // This runs on the UI thread before the background thread executes.
            super.onPreExecute();
            // Do pre-thread tasks such as initializing variables.

            Log.v("myBackgroundTask", "Starting Background Task");
        }

        @Override
        protected Void doInBackground(int[]...v) {
            int[] inputs=v[0];
            Log.i("Before running python object","line 71");
            Python py = Python.getInstance();
            Log.i("Before running PyObject","line 73");
            PyObject pyf = py.getModule("run_speedtest");
            Log.i("After running PyObject","line 75");
            PyObject obj = pyf.callAttr("main", inputs[0], inputs[1]);
            Log.i("After running obj","line 77");
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String results) returned from the doInBackground() method.
            // Update UI with the found string.
            super.onPostExecute(results);

            Toast.makeText(Speed.this, "All Done!", Toast.LENGTH_LONG).show();
        }
    }

}
