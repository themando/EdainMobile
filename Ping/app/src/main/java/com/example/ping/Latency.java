package com.example.ping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
//import com.example.ping.PingTrancoSites;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import static java.lang.Integer.parseInt;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.ping.Wifi_Network_Info.model.WifiDataModel;
import com.example.ping.Wifi_Network_Info.viewmodel.WifiViewModel;

public class Latency extends AppCompatActivity {
    private static final String FILE_NAME = "ping.csv";
    static WifiViewModel model;
   // static int doc_ser;
    static String wifi_name = "Time Limit Exceeded to get Wifi Network from API";
    TableLayout tl;
    RelativeLayout tableLayout;
    TableRow tr;
    PingTrancoSites asyncPing = new PingTrancoSites();
    TextView newRow,anotherRow;
    ProgressBar progressBar;


    /**
     * Adding Firestore services:
     */ FirebaseFirestore db = FirebaseFirestore.getInstance();


    String[] sites = {"google.com","facebook.com","youtube.com","microsoft.com",
            "twitter.com","tmall.com","instagram.com","windowsupdate.com","netflix.com","linkedin.com","qq.com","baidu.com","wikipedia.org","apple.com","live.com","sohu.com","yahoo.com","amazon.com","doubleclick.net","googletagmanager.com","taobao.com","adobe.com","youtu.be","pinterest.com","360.cn","vimeo.com","jd.com","reddit.com","wordpress.com","weibo.com","office.com","bing.com","blogspot.com","sina.com.cn","goo.gl","github.com","zoom.us","googleusercontent.com","amazonaws.com","bit.ly","wordpress.org","vk.com","microsoftonline.com","fbcdn.net","xinhuanet.com","tumblr.com","godaddy.com","mozilla.org","msn.com","skype.com","google-analytics.com","nytimes.com","flickr.com","whatsapp.com","okezone.com","ytimg.com","dropbox.com","gravatar.com","soundcloud.com","europa.eu","myshopify.com","alipay.com","cnn.com","nih.gov","apache.org","t.co","csdn.net","twitch.tv","office365.com","yahoo.co.jp","w3.org","macromedia.com","theguardian.com","googlevideo.com","medium.com","ebay.com","forbes.com","sourceforge.net","bbc.co.uk","spotify.com","bongacams.com","google.com.hk","imdb.com","paypal.com","zhanqi.tv","panda.tv","naver.com","aliexpress.com","googleadservices.com","bbc.com","archive.org","cloudflare.com","googlesyndication.com","stackoverflow.com","github.io","amazon.in","yandex.ru","weebly.com","creativecommons.org","china.com.cn"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);

        // init GUI
        Button pingButton = (Button) findViewById(R.id.pingButton);
        Button exportButton = (Button) findViewById(R.id.exportPingButton);
        Button clearButton = (Button) findViewById(R.id.clearPingButton);
        progressBar = (ProgressBar) this.findViewById(R.id.progressbar);

        //init instance of WifiViewModel
        model = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(WifiViewModel.class);
        model.init();
        final double[] value = {SystemClock.elapsedRealtime()};
        model.getLiveData().observe(this, new Observer<WifiDataModel>() {
            @Override
            public void onChanged(WifiDataModel wifiDataModel) {
                wifi_name = wifiDataModel.getOrg() + " " + wifiDataModel.getCompany().getDomain();
            }
        });


        /** Click Ping Button **/
        pingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tableLayout = (RelativeLayout)findViewById(R.id.tablelayout);
                tl =(TableLayout)findViewById(R.id.table);

            //Remove pre-existing table, and cancel aysnc task if previously running:
                if(asyncPing!=null && asyncPing.getStatus() != AsyncTask.Status.FINISHED){Log.i("asyncPing!=null-","set to true");
                    asyncPing.cancel(true); }
                tl.removeAllViews();
            /**Start Pinging, call async function**/
                addHeaders();
                asyncPing = new PingTrancoSites();
                asyncPing.execute(sites);
                progressBar.setVisibility(View.INVISIBLE);
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

         // Aysnc function for pinging through all the sites
    public class PingTrancoSites extends AsyncTask<String[], String, Void> {
        Map<String, Object> m = new HashMap<>();
        Map<String,Object> M = new HashMap<>();
             Random random = new Random();
             int randomNumber = random.nextInt(999999999);
             int doc_ser = randomNumber;
             int num = 0;

             @Override
             protected void onPreExecute(){
            // This runs on the UI thread before the background thread executes.
            super.onPreExecute();
            saveData(doc_ser);
            // Do pre-thread tasks such as initializing variables.

            progressBar.setVisibility(View.VISIBLE);
            Log.v("myBackgroundTask", "Starting Background Task");
        }

        @Override
        protected Void doInBackground(String[]... sites) {


            if(isCancelled())
            { Log.i("iscancelled","set to true"); return null;}
            String results;
            String[] site = sites[0];
            for (int i = 0; i<site.length ; i++) {
               // site[i]="www."+ site[i];
                if(isCancelled())
                { Log.i("iscancelled","set to true"); return null;}

                InetAddress ip_host;
                String url_host = "https://www." + site[i].trim() + "/";
                try {
                    ip_host = InetAddress.getByName(new URL(url_host).getHost());
                } catch (UnknownHostException | MalformedURLException e) {
                    ip_host = null;
                }
                
                String resolved_ip = String.valueOf(ip_host);
                Map<String, Object> m1 = new HashMap<>();
                m1.put("resolved_ip", resolved_ip);

                db.collection("latency").document(String.valueOf(doc_ser))
                        .collection("metric").document(site[i]).set(m1)
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


                String cmd = "/system/bin/ping -c " + "1" + " " + site[i];
                    Log.i("site:", site[i]);
                    Process p = executeCmd(cmd);
                    String pingStats = getPingStats(p);
                    pingStats = formatStats(pingStats, site[i], "1");
                    if (pingStats != null) {
                        results = pingStats;
                        Log.i("results after pinging Tranco site", results);
                        writeCsv(results, site[i], "1");

                        String[] resList = results.split(",");
                        m.put("min",resList[4]);
                        m.put("avg",resList[5]);
                        m.put("max",resList[6]);

                        M.put("latency",m);
                        db.collection("latency").document(String.valueOf(doc_ser))
                                .collection("metric").document(site[i]).set(M)
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
                        if (parseInt(results.split(",")[3]) == 100) {
                            results = "100% Loss :(";
                        }
                    } else {
                        Log.i("null situation", site[i]);
                        results = "Oops, site unreachable";
                    }

                    publishProgress("We're still pinging .. Please wait!", results, site[i], Integer.toString(i));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String...p) {
            // Runs on the UI thread after publishProgress is invoked
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(Latency.this, p[0], Toast.LENGTH_SHORT).show();
            addData(p[2], p[1]);
            Log.i("After adding row", p[1]);

        }

        @Override
        protected void onPostExecute(Void results) {
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String results) returned from the doInBackground() method.
            // Update UI with the found string.
            super.onPostExecute(results);
            if(asyncPing!=null && asyncPing.getStatus() != AsyncTask.Status.FINISHED){Log.i("asyncPing!=null-","set to true");
                asyncPing.cancel(true); }
            progressBar.setVisibility(View.GONE);

            //Toast.makeText(this, "All Done!", Toast.LENGTH_LONG).show();
        }
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

    /** Write command output to csv
     // Initialize new csv file if one does not exist
     // String output: output from environ command
     **/
    public void writeCsv(String output, String server, String pckts) {
        FileOutputStream fos = null;
        String headers = "Timestamp,Server,Packets[default = 1],Loss[%],Min[in ms],Avg[in ms],Max[in ms],Stddev\n";
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
            Log.i("toast res: ",res);
            //  Toast.makeText(this, res, Toast.LENGTH_LONG).show();
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
        if(lines.length==1){
            res = null;
            return res;
        }
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
        Log.i("res",res);
        return res;
    }

    // Read and return output stats from ping command
    // Append Timestamp to end of output
    // Process p: returned after executing command
    // Return 3 lines: Ping Server Success
    // Return 2 lines: Ping Server Fail
    public static String getPingStats(Process p) {

        // 1 packet: Server Success
        /**
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

    //Add Headers to the tableLayout
    public void addHeaders(){

        /** Create a TableRow dynamically **/

        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the newRow **/

        TextView newRow = new TextView(this);

        newRow.setText("Tranco Top 100:");

        newRow.setTextColor(Color.BLACK);
        newRow.setAllCaps(true);
        newRow.setTextSize(18);

        newRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        newRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        newRow.setPadding(50, 100, 5, 0);

        tr.addView(newRow);  // Adding textView to TableRow.

        /** Creating another textview **/

        TextView anotherRow = new TextView(this);

        anotherRow.setText("Avg.Time(in ms):");

        anotherRow.setTextColor(Color.BLACK);
        anotherRow.setAllCaps(true);
        anotherRow.setTextSize(18);

        anotherRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        anotherRow.setPadding(1, 100, 5, 0);

        anotherRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(anotherRow); // Adding textView to TableRow.

        /** Add the TableRow to the TableLayout **/

        tl.addView(tr, new TableLayout.LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the divider because we have two columns

        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        /** Creating another textview **/

        TextView divider = new TextView(this);

        divider.setText("-----------------------------");

        divider.setTextColor(Color.BLACK);

        divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        divider.setPadding(50, 0, 0, 0);

        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(divider); // Adding textView to TableRow.

        TextView divider2 = new TextView(this);

        divider2.setText("-------------------------");

        divider2.setTextColor(Color.BLACK);

        divider2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        divider2.setPadding(0, 0, 0, 0);

        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(divider2); // Adding textView to TableRow.

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

    }

    // Adds rows dynamically as the results from Ping come through
    public void addData(String site, String result) {

        /** Create a TableRow dynamically **/

        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the newRow **/

        newRow = new TextView(this);

        newRow.setText(site);

        newRow.setTextColor(Color.DKGRAY);

        newRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        newRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        newRow.setPadding(50, 5, 5, 5);

        tr.addView(newRow);  // Adding textView to TableRow.


        /** Creating another textview **/

        anotherRow = new TextView(this);
        String[] tmp = result.split(",");

        if(result.split(",").length > 2) {
            anotherRow.setText(tmp[5]); anotherRow.setTextColor(Color.DKGRAY);
        }
        else if (tmp.length<3){
            Log.i("site unreachable/loss 100%", Arrays.toString(tmp));
            if(tmp.length==2){anotherRow.setText(result); anotherRow.setTextColor(Color.BLUE);}
            else{
                anotherRow.setText(result); anotherRow.setTextColor(Color.RED);}
        }

        anotherRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        anotherRow.setPadding(5, 5, 5, 5);

        anotherRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(anotherRow); // Adding textView to TableRow.

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

    }

    private void saveData(int doc_ser) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy | hh:mm:ss");
        final String[] lte = new String[1];
        final String[] wifi = new String[1];
        String totalURLrange;
        if (this.sites != null) {
            totalURLrange = String.valueOf(this.sites.length);
        } else {
            totalURLrange = String.valueOf(1);
        }
        String timestamp = simpleDateFormat.format(new Date());

        if (isWifi(this)) {
            new Thread(new Runnable() {
               @Override
                public void run() {
                    WifiTask task = new WifiTask();
                    task.execute();
                    try {
                        Thread.sleep(5000);
                        wifi[0] = Latency.wifi_name;
//                        System.out.println(wifi[0]);
//                        System.out.println(SystemClock.elapsedRealtime());
                        lte[0] = "NN";
                        Map<String, Object> latency = new HashMap<>();
                        latency.put("lteNetwork", lte[0]);
                        latency.put("wifiNetwork", wifi[0]);
                        latency.put("timestamp", timestamp);
                        latency.put("totalURLRange", totalURLrange);
                        latency.put("serial", String.valueOf(doc_ser));

                        db.collection("latency").document(String.valueOf(doc_ser))
                                .set(latency)
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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            wifi[0] = "NN";
            lte[0] = getNetworkClass(getApplicationContext());
            Map<String, Object> latency = new HashMap<>();
            latency.put("lteNetwork", lte[0]);
            latency.put("wifiNetwork", wifi[0]);
            latency.put("timestamp", timestamp);
            latency.put("totalURLRange", totalURLrange);
            latency.put("serial", String.valueOf(doc_ser));

            db.collection("latency").document(String.valueOf(doc_ser))
                    .set(latency)
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
        }

    }

    public boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String format;
        if (info == null || !info.isConnected())
            return "-"; // not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:     // api< 8: replace by 11
                case TelephonyManager.NETWORK_TYPE_GSM:      // api<25: replace by 16
                    format = String.format("2G / %s", manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:   // api< 9: replace by 12
                case TelephonyManager.NETWORK_TYPE_EHRPD:    // api<11: replace by 14
                case TelephonyManager.NETWORK_TYPE_HSPAP:    // api<13: replace by 15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA: // api<25: replace by 17
                    format = String.format("3G / %s", manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_LTE:      // api<11: replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:    // api<25: replace by 18
                case 19: // LTE_CA
                    format = String.format("4G / %s", manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_NR:       // api<29: replace by 20
                    format = String.format("5G / %s", manager.getNetworkOperatorName());
                    return format;
                default:
                    format = String.format("%s", manager.getNetworkOperatorName());
                    return format;
            }
        }
        format = String.format("%s", manager.getNetworkOperatorName());
        return format;
    }

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return "NN";
    }

    private class WifiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Latency.model.fetchLiveData();
            return null;
        }
    }



}
