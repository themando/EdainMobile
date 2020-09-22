package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Ping extends Activity {
    private static final String FILE_NAME = "ping.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        // init GUI
        Button pingButton = (Button) findViewById(R.id.pingButton);
        Button exportButton = (Button) findViewById(R.id.exportPingButton);
        Button clearButton = (Button) findViewById(R.id.clearPingButton);
        final EditText servText = (EditText) findViewById(R.id.serverEditText);
        final EditText pcktsText = (EditText) findViewById(R.id.numPcktsEditText);

        // Click Ping Button
        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String server = servText.getText().toString();
                String numPckts = pcktsText.getText().toString();

                // Ignore button if no server specified
                if (server.isEmpty()) {
                    return;
                }

                // Ignore button if invalid num packets
                if (numPckts.isEmpty() || Integer.parseInt(numPckts) <= 0) {
                    return;
                }

                String cmd = "/system/bin/ping -c "+numPckts+" "+server;
                Process p = executeCmd(cmd);
                String pingStats = getPingStats(p);
                pingStats = formatStats(pingStats, server, numPckts);
                writeCsv(pingStats, server, numPckts);
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

    // Export csv file
    public void export() {

        try {
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), FILE_NAME);
            Uri path = FileProvider.getUriForFile(context, "com.example.Ping.fileprovider", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Ping Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Export ping.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Read and return data from file_name
    // Return data as a string
    // Return empty string if file_name does not exist
    public String load(String file_name) {
        String data = "";
        FileInputStream fis = null;


        // Ignore if no export file exists
        File file = new File(getFilesDir()+"/"+file_name);
        if (!file.exists()) {
            Toast.makeText(this, "Export file not found", Toast.LENGTH_LONG).show();
            return data;
        }

        try {
            fis = openFileInput(file_name);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            data = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    // Remove csv file
    public void deleteCsv() {
        boolean res = deleteFile(FILE_NAME);
        Toast.makeText(this, "ping.csv cleared", Toast.LENGTH_LONG).show();
    }

    // Write command output to csv
    // Initialize new csv file if one does not exist
    // String output: output from environ command
    public void writeCsv(String output, String server, String pckts) {
        FileOutputStream fos = null;
        String headers = "Timestamp,Server,Packets,Loss,Min,Avg,Max,Stddev\n";
        String res = "ping -c "+pckts+" "+server+"\n";
        String[] stats = output.split(",");
        String loss = stats[3];
        res += loss+"% packet loss";
        boolean initCsv = false;

        // Initialize csv file if one does not exist
        File file = new File(getFilesDir()+"/"+FILE_NAME);
        if (!file.exists()) {
            initCsv = true;
        }

        try {
            fos = new FileOutputStream(file, true);
            if (initCsv) {
                fos.write(headers.getBytes());
            }
            fos.write(output.getBytes());
            Toast.makeText(this, output, Toast.LENGTH_LONG).show();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("WRITE", "Error writing to csv.");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Format ping statistics from ping command output
    // Return string in the following field order:
    // "Timestamp,Server,Packets,Loss,Min,Avg,Max,Stddev"
    public static String formatStats(String stats, String server, String packets) {
        String res, time, loss, min, avg, max, sd;
        res = time = loss = min = avg = max = sd = "";

        String[] lines = stats.split("\n");
        String[] line1 = lines[0].split(", ");
        time = lines[2];
        loss = line1[2].substring(0,line1[2].indexOf("%"));

        // Grab rtt stats from ping server success cmd
        if (lines[1].contains("rtt")) {
            String[] line2 = lines[1].split(" = ");
            String[] rtt = line2[1].split("/");
            min = rtt[0];
            avg = rtt[1];
            max = rtt[2];
            sd = rtt[3].substring(0,rtt[3].length()-3);
        }

        res += time+","+server+","+packets+","+loss+",";
        res += min+","+avg+","+max+","+sd+"\n";
        return res;
    }

    // Read and return output stats from ping command
    // Append Timestamp to end of output
    // Process p: returned after executing command
    // Return 3 lines: Ping Server Success
    // Return 2 lines: Ping Server Fail
    public static String getPingStats(Process p) {

        // 1 packet: Server Success
        /*
            PING 8.8.8.8 (8.8.8.8) 56(84) bytes of data.
            64 bytes from 8.8.8.8: icmp_seq=1 ttl=254 time=38.5 ms
            --- 8.8.8.8 ping statistics ---
            1 packets transmitted, 1 received, 0% packet loss, time 0ms
            rtt min/avg/max/mdev = 38.591/38.591/38.591/0.000 ms
         */

        // 1 packet: Server Fail
        /*
            PING asdf (92.242.140.2) 56(84) bytes of data.
            --- asdf ping statistics ---
            1 packets transmitted, 0 received, 100% packet loss, time 0ms
         */

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utc = f.format(new Date());

        try {
            String s;
            String res = "";
            boolean foundStats = false;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                if (foundStats) {
                    res += s + "\n";
                } else if (s.contains("statistics")) {
                    foundStats = true;
                }
            }
            res += utc;
            p.destroy();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("READ", "Error reading process from command.");
        }
        return "";
    }

    // Execute the environ command and return the resulting Process object
    // Process obj is null if command does not execute properly
    public static Process executeCmd(String cmd){
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("EXECUTE", "Error executing command.");
        }
        return p;
    }

}
