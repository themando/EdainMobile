package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pagespeedonline.Pagespeedonline;
import com.google.api.services.pagespeedonline.model.PagespeedApiLoadingExperienceV5;
import com.google.api.services.pagespeedonline.model.PagespeedApiPagespeedResponseV5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;


public class Page extends AppCompatActivity {

    private static final String GOOGLE_KEY = "AIzaSyBhrBJpeRJqPdOcf4QZy-zPz91el80Io_0";
    private static final String HEADERS = "County,Location,Provider,UTC,URL,First_Contentful_Paint,First_Input_Delay\n";
    private static final String FILE_NAME = "plt.csv";

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

        // Load Button
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

                // Iterative PageSpeed Insights for urls
                try {
                    PagespeedApiPagespeedResponseV5 insight = null;
                    ArrayList<String> results = new ArrayList<>();
                    String result = "";
                    String url = "";
                    int idx;

                    FileInputStream fis = openFileInput("top-1m.csv");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);

                    // gather Insight from each url
                    for (int i = 0; i < numUrlsInt; i++) {
                        result = county+","+location+","+provider+",";

                        url = br.readLine();
                        idx = url.indexOf(",")+1;
                        url = "https://"+url.substring(idx);

                        insight = getInsight(url, timeoutInt);
                        if (insight != null) {
                            result += formatInsight(insight);
                            results.add(result);
                        }
                    }
                    fis.close();

                    // write results to csv
                    writeCsv(results);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("API", "Error gathering Insights");
                }

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
        Toast.makeText(this, "plt.csv cleared", Toast.LENGTH_LONG).show();
    }

    // Export csv file
    public void export() {

        try {
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), FILE_NAME);
            Uri path = FileProvider.getUriForFile(context, "com.example.Ping.fileprovider", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Page Load Time Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Export plt.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Write results to csv one line at a time
    public void writeCsv(ArrayList<String> results) {
        FileOutputStream fos = null;
        boolean initCsv = false;

        // Initialize csv file if one does not exist
        File file = new File(getFilesDir()+"/"+FILE_NAME);
        if (!file.exists()) {
            initCsv = true;
        }

        try {
            fos = new FileOutputStream(file, true);
            if (initCsv) {
                fos.write(HEADERS.getBytes());
            }
            for (int i = 0; i < results.size(); i++) {
                fos.write(results.get(i).getBytes());
            }
            Toast.makeText(this, "Finished collecting page load times.", Toast.LENGTH_LONG).show();
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

    // Return comma separated string of important metrics from PageSpeed Insight
    // Current metrics - first content/input from loading experience
    String formatInsight(PagespeedApiPagespeedResponseV5 raw) {
        StringBuilder sb = new StringBuilder();

        // utc, url
        sb.append(raw.getAnalysisUTCTimestamp()+",");
        sb.append(raw.getId()+",");

        // Origin Loading Experience
        //PagespeedApiLoadingExperienceV5 origExp = raw.getOriginLoadingExperience();
        //Map<String, PagespeedApiLoadingExperienceV5.MetricsElement> origMetrics = origExp.getMetrics();
        //PagespeedApiLoadingExperienceV5.MetricsElement first_content_orig = origMetrics.get("FIRST_CONTENTFUL_PAINT_MS");
        //PagespeedApiLoadingExperienceV5.MetricsElement first_input_orig = origMetrics.get("FIRST_INPUT_DELAY_MS");

        // Loading Experience
        PagespeedApiLoadingExperienceV5 loadExp = raw.getLoadingExperience();
        Map<String, PagespeedApiLoadingExperienceV5.MetricsElement> loadMetrics = loadExp.getMetrics();
        PagespeedApiLoadingExperienceV5.MetricsElement first_content = loadMetrics.get("FIRST_CONTENTFUL_PAINT_MS");
        PagespeedApiLoadingExperienceV5.MetricsElement first_input = loadMetrics.get("FIRST_INPUT_DELAY_MS");
        sb.append(first_content.get("category")+",");
        sb.append(first_input.get("category"));

        sb.append("\n");
        return sb.toString();
    }

    // Return PageSpeed Insight results from one address
    // Return null on failure
    PagespeedApiPagespeedResponseV5 getInsight(String url, final int timeout) {
        Pagespeedonline p;
        Pagespeedonline.Pagespeedapi.Runpagespeed runpagespeed;
        PagespeedApiPagespeedResponseV5 response = null;
        JsonFactory jsonFactory = new JacksonFactory();
        HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();

        HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setReadTimeout(timeout*1000); // milliseconds
            }
        };

        p = new Pagespeedonline.Builder(transport, jsonFactory, httpRequestInitializer).setApplicationName("Edain Mobile").build();

        try {
            runpagespeed = p.pagespeedapi().runpagespeed(url).setKey(GOOGLE_KEY).setStrategy("mobile");
            response = runpagespeed.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("API", "Error executing PageSpeed Insights.");
        }

        return response;
    }

    // Gather PageSpeed Insight results from batch requests
    // ** This method failed a lot during testing **
    void getInsight_batch(String[] urls, int numUrls, final int timeout) {
        Pagespeedonline p;
        BatchRequest batch;

        // Callback to log batch status
        JsonBatchCallback<PagespeedApiPagespeedResponseV5> callback = new JsonBatchCallback<PagespeedApiPagespeedResponseV5>() {

            public void onSuccess(PagespeedApiPagespeedResponseV5 response, HttpHeaders responseHeaders) {
                // Collect response here if decide to use method
                System.out.println("Success");
            }
            public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                System.out.println("Fail");
            }
        };

        JsonFactory jsonFactory = new JacksonFactory();
        HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
        HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) {
                httpRequest.setReadTimeout(timeout*1000); // milliseconds
            }
        };

        p = new Pagespeedonline.Builder(transport, jsonFactory, httpRequestInitializer).setApplicationName("Edain Mobile").build();
        batch = p.batch(httpRequestInitializer);

        try {
            for (int i = 0; i < numUrls; i++) {
                Pagespeedonline.Pagespeedapi.Runpagespeed check = p.pagespeedapi().runpagespeed(urls[i]).setKey(GOOGLE_KEY).setStrategy("mobile");
                check.queue(batch, callback);
            }
            batch.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("API", "Error executing PageSpeed Insights batch request.");
        }
    }

}
