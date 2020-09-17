package com.example.ping.dns_resolution.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.ping.R;
import com.example.ping.dns_resolution.model.DnsModel;
import com.example.ping.dns_resolution.viewmodel.DnsViewModel;
import com.example.ping.wifi_resolution.model.WifiDataModel;
import com.example.ping.wifi_resolution.viewmodel.WifiViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DnsResolution extends Fragment {

    // init GUI
    static final String FILE_A_RECORDS = "a_record.csv";
    static final String FILE_SOA_RECORDS = "soa_record.csv";
    static final String FILE_MX_RECORDS = "mx_record.csv";
    static final String FILE_NS_RECORDS = "ns_record.csv";
    static final String FILE_TXT_RECORDS = "txt_record.csv";
    ArrayList<String>[] output = new ArrayList[5];

    // Top 10 Tranco Sites
    String[] top10TrancoSites = new String[]{"google.com", "facebook.com", "youtube.com", "twitter.com", "microsoft.com",
            "tmall.com", "instagram.com", "linkedin.com", "netflix.com", "windowsupdate.com"};

    Button pingButton, addButton, clearButton, exportButton, trancoButton;
    EditText editText;
    TextView aRecord, soaRecord, mxRecord, nsRecord, txtRecord;
    DnsViewModel viewModelARecord, viewModelSOARecord, viewModelMXRecord, viewModelNSRecord, viewModelTXTRecord;
    ScrollView scrollView;
    ProgressBar progressBar;
    String dnsServer;

    // val for deciding whether we want Top 10 Tranco Site Data or Data for URL
    boolean val = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dns_resolution, container, false);
        scrollView = view.findViewById(R.id.scrollView);
        progressBar = view.findViewById(R.id.progress_circular);
        editText = view.findViewById(R.id.edit_text);
        aRecord = view.findViewById(R.id.aRecord);
        soaRecord = view.findViewById(R.id.soaRecord);
        mxRecord = view.findViewById(R.id.mxRecord);
        nsRecord = view.findViewById(R.id.nsRecord);
        txtRecord = view.findViewById(R.id.txtRecord);
        pingButton = view.findViewById(R.id.dnsPingButton);
        addButton = view.findViewById(R.id.dnsAddButton);
        exportButton = view.findViewById(R.id.dnsExportButton);
        clearButton = view.findViewById(R.id.dnsClearButton);
        trancoButton = view.findViewById(R.id.dnsTrancoButton);

        // CLick Ping Button
        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val = false;
                // launch Progress Bar while retrieving Data
                scrollView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                final String url = editText.getText().toString();

                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // get DNS Records
                            Task task = new Task();
                            task.execute(url).get();

                            Thread.sleep(600);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    scrollView.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        // Click Add Button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeCsv(output);
            }
        });

        // Click Export Button
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportCsv();
            }
        });

        // Click Clear Button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCsv();
            }
        });

        // Click Tranco Button
        trancoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val = true;
                // launch Progress Bar while retrieving Data
                scrollView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // get DNS Records for Top 10 Tranco Sites
                        BackGroundTask task = new BackGroundTask();
                        task.execute();

                        try {
                            Thread.sleep(10000);
                            exportCsv();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "DNS Data for Top 10 Tranco Sites", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init ViewModels for fetching different records
        viewModelARecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelSOARecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelMXRecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelNSRecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelTXTRecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);

        // ViewModels Observe the Data Changes and store the values in output Array<String>

//        dnsServer = getDnsServer();
//        System.out.println(getNetworkName());
        getWifiData();
        // init ViewModel
        viewModelARecord.init();

        viewModelARecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter();

                // Checking if we have Click on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    aRecord.setText(dnsRecordFetcher.aRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[0] = dnsRecordFetcher.exportARecord(dnsModel);
            }
        });

        // init ViewModel
        viewModelSOARecord.init();

        viewModelSOARecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter();

                // Checking if we have Click on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    soaRecord.setText(dnsRecordFetcher.soaRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[1] = dnsRecordFetcher.exportSOARecord(dnsModel);
            }
        });

        // init ViewModel
        viewModelMXRecord.init();

        viewModelMXRecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter();

                // Checking if we have Click on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    mxRecord.setText(dnsRecordFetcher.mxRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[2] = dnsRecordFetcher.exportMXRecord(dnsModel);
            }
        });

        // init ViewModel
        viewModelNSRecord.init();

        viewModelNSRecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter();

                // Checking if we have Click on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    nsRecord.setText(dnsRecordFetcher.nsRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[3] = dnsRecordFetcher.exportNSRecord(dnsModel);
            }
        });

        // init ViewModel
        viewModelTXTRecord.init();

        viewModelTXTRecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {
                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter();

                // Checking if we have Click on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    txtRecord.setText(dnsRecordFetcher.txtRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[4] = dnsRecordFetcher.exportTXTRecord(dnsModel);
            }
        });
    }

    // Method for getting DNS Records
    public void getDnsRecords(String url) {
        String[] types = new String[]{"a", "soa", "mx", "ns", "txt"};

        // ViewModels fetching data for different types of records

        viewModelARecord.fetchData(url, types[0]);
        viewModelSOARecord.fetchData(url, types[1]);
        viewModelMXRecord.fetchData(url, types[2]);
        viewModelNSRecord.fetchData(url, types[3]);
        viewModelTXTRecord.fetchData(url, types[4]);
    }

    // delete CSV
    public void deleteCsv() {
        // file names for various files containing dns records

        String[] fileNames = new String[]{FILE_A_RECORDS, FILE_SOA_RECORDS, FILE_MX_RECORDS, FILE_NS_RECORDS, FILE_TXT_RECORDS};
        for (int i = 0; i < 5; i++) {
            requireContext().deleteFile(fileNames[i]);
        }

        // checking if we click on Ping Button

        if (!val) {
            // Toast for showing that files have been deleted
            Toast.makeText(getContext(), "dns_records cleared", Toast.LENGTH_SHORT).show();
        }
    }

    // Export CSV
    public void exportCsv() {
        try {
            Context context = requireActivity().getApplicationContext();

            // file names for various files containing dns records
            String[] fileNames = new String[]{FILE_A_RECORDS, FILE_SOA_RECORDS, FILE_MX_RECORDS, FILE_NS_RECORDS, FILE_TXT_RECORDS};

            // init file locations and paths
            File[] fileLocations = new File[5];
            Uri[] paths = new Uri[5];

            ArrayList<Uri> exportFiles = new ArrayList<>();
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "DNS Resolution Data");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            for (int i = 0; i < 5; i++) {
                fileLocations[i] = new File(requireContext().getFilesDir() + "/" + fileNames[i]);
                paths[i] = FileProvider.getUriForFile(context, "com.example.Ping.fileprovider", fileLocations[i]);
                exportFiles.add(paths[i]);
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, exportFiles);
            startActivity(Intent.createChooser(intent, "Export dns_records"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Write command output to csv
    // Initialize new csv file if one does not exist
    // String output: output from environ command
    public void writeCsv(ArrayList<String>[] output) {
        FileOutputStream fileOutputStream = null;
        String[] headers = new String[5];
        headers[0] = "IPAddress\n";
        headers[1] = "PrimaryServer,Administrator,SerialNumber,Refresh,Retry,Expire,TTL\n";
        headers[2] = "IPAddress,Preferences,DomainName\n";
        headers[3] = "DomainName,IPAddress\n";
        headers[4] = "Data\n";
        String[] fileNames = new String[]{FILE_A_RECORDS, FILE_SOA_RECORDS, FILE_MX_RECORDS, FILE_NS_RECORDS, FILE_TXT_RECORDS};
        boolean[] initRecords = new boolean[]{false, false, false, false, false};

        // init file locations
        File[] fileLocations = new File[5];

        // Initialize csv file if one does not exist
        for (int i = 0; i < 5; i++) {
            fileLocations[i] = new File(requireContext().getFilesDir() + "/" + fileNames[i]);
            if (!fileLocations[i].exists()) {
                initRecords[i] = true;
            }
        }
        for (int i = 0; i < 5; i++) {
            try {
                fileOutputStream = new FileOutputStream(fileLocations[i], true);
                if (initRecords[i]) {
                    fileOutputStream.write(headers[i].getBytes());
                }
                for (int j = 0; j < output[i].size(); j++) {
                    fileOutputStream.write(output[i].get(j).getBytes());
                }
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("WRITE", "Error Writing csv");
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // get DNS Records for Top 10 Tranco Sites
    public void Top10TrancoSiteDatatoCsv() {
        deleteCsv();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Task task = new Task();
                    task.execute(top10TrancoSites[i]);
                }
            }
        }).start();
    }

    // Async Task for getting DNS Records in the Background
    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            getDnsRecords(strings[0]);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            writeCsv(output);
        }
    }

    // Async Task for getting DNS Record for Top 10 Sites in Background
    @SuppressLint("StaticFieldLeak")
    private class BackGroundTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Top10TrancoSiteDatatoCsv();
            return true;
        }
    }

    public String getDnsServer(){
        StringBuilder builder = new StringBuilder();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info.isConnected()){
            LinkProperties linkProperties    = connectivityManager.getLinkProperties(network);

            List<InetAddress> dnsServersList = linkProperties.getDnsServers();
            for(InetAddress element: dnsServersList){
                builder.append(element.getHostAddress() + "\n");
                break;
            }
        }

        return  builder.toString();
    }

    public String getNetworkName(){
        StringBuilder builder = new StringBuilder();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo.isConnected()){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                WifiManager wifiMgr = (WifiManager) this.getContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                builder.append(wifiInfo.getSSID());
            }
            else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                TelephonyManager manager = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                builder.append(manager.getNetworkOperatorName());
            }
        }
        else{
            builder.append("null");
        }

        return builder.toString();
    }

    public void getWifiData(){
        WifiViewModel model = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(WifiViewModel.class);

        final StringBuilder stringBuilder = new StringBuilder();
        model.init();
        model.getLiveData().observe(this, new Observer<WifiDataModel>() {
            @Override
            public void onChanged(WifiDataModel wifiDataModel) {
                stringBuilder.append(wifiDataModel.getOrg()).append(" ").append(wifiDataModel.getCompany().getDomain());
                dnsServer = stringBuilder.toString();
                System.out.println(dnsServer);
            }
        });
    }

    public void storeToFireStore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> mp = new Map<String, Object>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(@Nullable Object key) {
                return false;
            }

            @Override
            public boolean containsValue(@Nullable Object value) {
                return false;
            }

            @Nullable
            @Override
            public Object get(@Nullable Object key) {
                return null;
            }

            @Nullable
            @Override
            public Object put(String key, Object value) {
                return null;
            }

            @Nullable
            @Override
            public Object remove(@Nullable Object key) {
                return null;
            }

            @Override
            public void putAll(@NonNull Map<? extends String, ?> m) {

            }

            @Override
            public void clear() {

            }

            @NonNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NonNull
            @Override
            public Collection<Object> values() {
                return null;
            }

            @NonNull
            @Override
            public Set<Entry<String, Object>> entrySet() {
                return null;
            }
        };
    }
}