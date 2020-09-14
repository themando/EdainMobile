/*
The code given at https://github.com/olivierg13/TraceroutePing was used as a reference
 */

package com.example.ping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class TracerouteWithPing {

    private static final String PING = "PING";
    private static final String FROM_PING = "From";
    private static final String SMALL_FROM_PING = "from";
    private static final String PARENTHESE_OPEN_PING = "(";
    private static final String PARENTHESE_CLOSE_PING = ")";
    private static final String TIME_PING = "time=";
    private static final String EXCEED_PING = "exceed";
    private static final String UNREACHABLE_PING = "100%";

    private TracerouteContainer latestTrace;
    private int ttl;
    private int finishedTasks;
    private String url;
    private ArrayList<String> urls;
    private String ipToPing;
    private TraceActivity context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Random random = new Random();
    int randomNumber = random.nextInt(999999999);
    int doc_ser = randomNumber;
    int num = 0;

    // timeout handling
    private static final int TIMEOUT = 30000;
    private Handler handlerTimeout;
    private static Runnable runnableTimeout;

    public TracerouteWithPing(TraceActivity context) {
        this.context = context;
    }

    /**
     * Launches the Traceroute
     *
     * @param url    The url to trace
     * @param maxTtl The max time to live to set (ping param)
     */
    public void executeTraceroute(String url, int maxTtl) {
        this.ttl = 1;
        this.finishedTasks = 0;
        this.url = url;

        saveData();
        InetAddress ip_host;
        String url_host = "https://" + url.trim() + "/";
        try {
            ip_host = InetAddress.getByName(new URL(url_host).getHost());
        } catch (UnknownHostException | MalformedURLException e) {
            ip_host = null;
        }
        String resolved_ip = String.valueOf(ip_host);
        Map<String, Object> m = new HashMap<>();
        m.put("resolved_ip", resolved_ip);

        db.collection("traceroute").document(String.valueOf(doc_ser))
                .collection("metric").document(url).set(m)
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

        new ExecutePingAsyncTask(maxTtl, url).execute();
    }

    public void executeTrancoTraceroute(int maxTtl, ArrayList<String> urls) {
        this.ttl = 1;
        this.finishedTasks = 0;
        this.urls = urls;

        saveData();

        for (int i = 0; i < urls.size(); i++) {
            String url_host = "https://" + urls.get(i).trim() + "/";
            InetAddress ip_host;
            try {
                ip_host = InetAddress.getByName(new URL(url_host).getHost());
            } catch (UnknownHostException | MalformedURLException e) {
                ip_host = null;
            }
            String resolved_ip = String.valueOf(ip_host);
            Map<String, Object> m = new HashMap<>();
            m.put("resolved_ip", resolved_ip);
            db.collection("traceroute").document(String.valueOf(doc_ser))
                    .collection("metric").document(urls.get(i)).set(m)
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

        for (int i = 0; i < urls.size(); i++) {
            new ExecutePingAsyncTask(maxTtl, String.valueOf(urls.get(i))).execute();
        }
    }

    /**
     * Allows to timeout the ping if TIMEOUT exceeds. (-w and -W are not always supported on Android)
     */
    private class TimeOutAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExecutePingAsyncTask task;
        private int ttlTask;

        public TimeOutAsyncTask(ExecutePingAsyncTask task, int ttlTask) {
            this.task = task;
            this.ttlTask = ttlTask;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (handlerTimeout == null) {
                handlerTimeout = new Handler();
            }

            // stop old timeout
            if (runnableTimeout != null) {
                handlerTimeout.removeCallbacks(runnableTimeout);
            }
            // define timeout
            runnableTimeout = new Runnable() {
                @Override
                public void run() {
                    if (task != null) {
                        Log.e(TraceActivity.tag, ttlTask + " task.isFinished()" + finishedTasks + " " + (ttlTask == finishedTasks));
                        if (ttlTask == finishedTasks) {
                            Toast.makeText(context, context.getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                            task.setCancelled(true);
                            task.cancel(true);
                            context.stopProgressBar();
                        }
                    }
                }
            };
            // launch timeout after a delay
            handlerTimeout.postDelayed(runnableTimeout, TIMEOUT);

            super.onPostExecute(result);
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

    private void saveData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy | hh:mm:ss");
        String lte;
        String wifi;
        String totalURLrange;
        if (this.urls != null) {
            totalURLrange = String.valueOf(this.urls.size());
        } else {
            totalURLrange = String.valueOf(1);
        }
        String timestamp = simpleDateFormat.format(new Date());

        if (isWifi(context)) {
            wifi = getWifiName(context);
            lte = "NN";
        } else {
            wifi = "NN";
            lte = getNetworkClass(context);
        }

        Map<String, Object> traceroute = new HashMap<>();
        traceroute.put("lteNetwork", lte);
        traceroute.put("wifiNetwork", wifi);
        traceroute.put("timestamp", timestamp);
        traceroute.put("totalURLRange", totalURLrange);
        traceroute.put("serial", String.valueOf(doc_ser));

        db.collection("traceroute").document(String.valueOf(doc_ser))
                .set(traceroute)
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

    /**
     * The task that ping an ip, with increasing time to live (ttl) value
     */
    private class ExecutePingAsyncTask extends AsyncTask<Void, Void, String> {

        private boolean isCancelled;
        private int maxTtl;
        private String urlToPing;

        public ExecutePingAsyncTask(int maxTtl, String urlToPing) {
            this.maxTtl = maxTtl;
            this.urlToPing = urlToPing;
        }

        /**
         * Launches the ping, launches InetAddress to retrieve url if there is one, store trace
         */
        @SuppressLint("DefaultLocale")
        @Override
        protected String doInBackground(Void... params) {
            if (hasConnectivity()) {
                try {
                    num++;
                    String res = launchPing(urlToPing);
                    String[] stats = res.split("\n");
                    TracerouteContainer trace;
                    String ip = parseIpFromPing(res);
                    String time = stats[stats.length - 1];

//                    if (res.contains(UNREACHABLE_PING) && !res.contains(EXCEED_PING))
                    if (time.contains("min/avg/max/mdev = ")) {
                        String latencies = time.split("min/avg/max/mdev = ")[1];
                        String[] times = latencies.split("/");
                        String min = times[0];
                        String avg = times[1];
                        String max = times[2];

                        trace = new TracerouteContainer("", urlToPing, ip, true,
                                "min=" + min + "ms, max=" + max + "ms, avg=" + avg + "ms");
                        InetAddress inetAddr = InetAddress.getByName(trace.getIp());
                        String hostname = inetAddr.getHostName();
                        Map<String, Object> m_1 = new HashMap<>();
                        m_1.put("max", max);
                        m_1.put("min", min);
                        m_1.put("avg", avg);
                        Map<String, Object> m_2 = new HashMap<>();
                        m_2.put("hop_url", hostname);
                        m_2.put("hop_ip", trace.getIp());
                        m_2.put("latency", m_1);
                        db.collection("traceroute").document(String.valueOf(doc_ser))
                                .collection("metric").document(urlToPing).
                                collection("routing").document(String.format("hop-%d",num)).set(m_2)
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

                    } else {
                        // Gateway is down
                        trace = new TracerouteContainer("", urlToPing, ip, false, "Oops, Time to live was exceeded");
                        Map<String, Object> m_1 = new HashMap<>();
                        InetAddress inetAddr = InetAddress.getByName(trace.getIp());
                        String hostname = inetAddr.getHostName();
                        m_1.put("max", "Time to live was exceeded");
                        m_1.put("min", "Time to live was exceeded");
                        m_1.put("avg", "Time to live was exceeded");
                        Map<String, Object> m_2 = new HashMap<>();
                        m_2.put("hop_url", hostname);
                        m_2.put("hop_ip", trace.getIp());
                        m_2.put("latency", m_1);
                        db.collection("traceroute").document(String.valueOf(doc_ser))
                                .collection("metric").document(urlToPing).
                                collection("routing").document(String.format("hop-%d",num)).set(m_2)
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

                    // Get the host name from ip (unix ping do not support
                    // hostname resolving)
                    InetAddress inetAddr = InetAddress.getByName(trace.getIp());
                    String hostname = inetAddr.getHostName();
                    String canonicalHostname = inetAddr.getCanonicalHostName();
                    trace.setHostname(hostname);
                    latestTrace = trace;
                    Log.d(TraceActivity.tag, "hostname : " + hostname);
                    Log.d(TraceActivity.tag, "canonicalHostname : " + canonicalHostname);

                    // Store the TracerouteContainer object
                    Log.d(TraceActivity.tag, trace.toString());

                    // Not refresh list if this ip is the final ip but the ttl is not maxTtl
                    // this row will be inserted later
                    if (!ip.equals(ipToPing) || ttl == maxTtl) {
                        context.refreshList(trace);
                    }

                    return res;
                } catch (final Exception e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onException(e);
                        }
                    });
                }
            } else {
                return context.getString(R.string.no_connectivity);
            }
            return "";
        }


        /**
         * Launches ping command
         *
         * @param url The url to ping
         * @return The ping string
         */
        @SuppressLint("NewApi")
        private String launchPing(String url) throws Exception {
            // Build ping command with parameters
            Process p;
            String command = "";

            String format = "ping -c 3 -t %d ";
            command = String.format(format, ttl);

            Log.d(TraceActivity.tag, "Will launch : " + command + url);

            long startTime = System.nanoTime();
            // timeout task
            new TimeOutAsyncTask(this, ttl).execute();
            // Launch command
            p = Runtime.getRuntime().exec(command + url);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // Construct the response from ping
            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }

            p.destroy();

            if (res.equals("")) {
                throw new IllegalArgumentException();
            }

            // Store the wanted ip adress to compare with ping result
            if (ttl == 1) {
                ipToPing = parseIpToPingFromPing(res);
            }

            return res;
        }

        /**
         * Treat the previous ping (launches a ttl+1 if it is not the final ip, refresh the list on view etc...)
         */
        @Override
        protected void onPostExecute(String result) {
            if (!isCancelled) {
                try {
                    if (!"".equals(result)) {
                        if (context.getString(R.string.no_connectivity).equals(result)) {
                            Toast.makeText(context, context.getString(R.string.no_connectivity), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TraceActivity.tag, result);

                            if (latestTrace != null && latestTrace.getIp().equals(ipToPing)) {
                                if (ttl < maxTtl) {
                                    ttl = maxTtl;
                                    new ExecutePingAsyncTask(maxTtl, urlToPing).execute();
                                } else {
                                    context.stopProgressBar();
                                }
                            } else {
                                if (ttl < maxTtl) {
                                    ttl++;
                                    new ExecutePingAsyncTask(maxTtl, urlToPing).execute();
                                }
                            }
//							context.refreshList(traces);
                        }
                    }
                    finishedTasks++;
                } catch (final Exception e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onException(e);
                        }
                    });
                }
            }

            super.onPostExecute(result);
        }

        /**
         * Handles exception on ping
         *
         * @param e The exception thrown
         */
        private void onException(Exception e) {
            Log.e(TraceActivity.tag, e.toString());

            if (e instanceof IllegalArgumentException) {
                Toast.makeText(context, context.getString(R.string.no_ping), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
            }

            context.stopProgressBar();

            finishedTasks++;
        }

        public void setCancelled(boolean isCancelled) {
            this.isCancelled = isCancelled;
        }

    }

    /**
     * Gets the ip from the string returned by a ping
     *
     * @param ping The string returned by a ping command
     * @return The ip contained in the ping
     */
    private String parseIpFromPing(String ping) {
        String ip = "";
        if (ping.contains(FROM_PING)) {
            // Get ip when ttl exceeded
            int index = ping.indexOf(FROM_PING);

            ip = ping.substring(index + 5);
            if (ip.contains(PARENTHESE_OPEN_PING)) {
                // Get ip when in parenthese
                int indexOpen = ip.indexOf(PARENTHESE_OPEN_PING);
                int indexClose = ip.indexOf(PARENTHESE_CLOSE_PING);

                ip = ip.substring(indexOpen + 1, indexClose);
            } else {
                // Get ip when after from
                ip = ip.substring(0, ip.indexOf("\n"));
                if (ip.contains(":")) {
                    index = ip.indexOf(":");
                } else {
                    index = ip.indexOf(" ");
                }

                ip = ip.substring(0, index);
            }
        } else {
            // Get ip when ping succeeded
            int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
            int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);

            ip = ping.substring(indexOpen + 1, indexClose);
        }

        return ip;
    }

    /**
     * Gets the final ip we want to ping (example: if user fullfilled google.fr, final ip could be 8.8.8.8)
     *
     * @param ping The string returned by a ping command
     * @return The ip contained in the ping
     */
    private String parseIpToPingFromPing(String ping) {
        String ip = "";
        if (ping.contains(PING)) {
            // Get ip when ping succeeded
            int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
            int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);

            ip = ping.substring(indexOpen + 1, indexClose);
        }

        return ip;
    }

    /**
     * Gets the time from ping command (if there is)
     *
     * @param ping The string returned by a ping command
     * @return The time contained in the ping
     */
    private String parseTimeFromPing(String ping) {
        String time = "";
        if (ping.contains(TIME_PING)) {
            int index = ping.indexOf(TIME_PING);

            time = ping.substring(index + 5);
            index = time.indexOf(" ");
            time = time.substring(0, index);
        }

        return time;
    }

    /**
     * Check for connectivity (wifi and mobile)
     *
     * @return true if there is a connectivity, false otherwise
     */
    public boolean hasConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}