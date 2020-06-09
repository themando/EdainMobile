package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

// WebPageTest is used for measuring and analyzing the performance of web pages.
// https://sites.google.com/a/webpagetest.org/docs/
//
// The API key is limited to 200 page loads per day.
// If need to do more testing then should consider running a private instance:
// https://sites.google.com/a/webpagetest.org/docs/private-instances

public class Page extends AppCompatActivity {

    private static final String API_KEY = "A.4d4a2d75f8fd7cd5eb23b9304071c1db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        // init GUI
        Button loadButton = (Button) findViewById(R.id.loadButton);
        final EditText urlText = (EditText) findViewById(R.id.urlEditText);
        final EditText timeoutText = (EditText) findViewById(R.id.timeoutEditText);
        final EditText countyText = (EditText) findViewById(R.id.countyEditText);
        final EditText locationText = (EditText) findViewById(R.id.locationEditText);
        final EditText providerText = (EditText) findViewById(R.id.providerEditText);

        // Load Button
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int timeoutInt;
                String timeoutStr = timeoutText.getText().toString();
                final String test_url = urlText.getText().toString();
                final String county = countyText.getText().toString();
                final String location = locationText.getText().toString();
                final String provider = providerText.getText().toString();

                // Invalid inputs - empty
                if (test_url.isEmpty() || timeoutStr.isEmpty() || county.isEmpty()
                        || location.isEmpty() || provider.isEmpty()) {
                    return;
                } else {
                    timeoutInt = Integer.parseInt(timeoutStr);
                }

                // Invalid inputs - values
                if (timeoutInt <= 0) {
                    return;
                }

                // Set WebView to download files from browser to sdcard/Download
                WebView wv = new WebView(Page.this);
                wv.setDownloadListener(new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        String test_site = test_url.substring(4,test_url.indexOf(".com"));
                        String title = test_site+"_"+county+"_"+location+"_"+provider+".csv";

                        request.setMimeType(mimeType);
                        String cookies = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookies);
                        request.addRequestHeader("User-Agent", userAgent);
                        request.setDescription("Downloading file...");
                        request.setTitle(title);
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                    }
                });


                // Run WebPageTest
                try {
                    String testId;
                    String statusText = "Test Incomplete";
                    String run;
                    JSONObject run_json;
                    String test;
                    JSONObject test_json;

                    // run test
                    run = getResponseText("http://www.webpagetest.org/runtest.php?url="+
                            test_url+"&f=json&k="+ API_KEY);
                    run_json = new JSONObject(run);
                    testId = run_json.getJSONObject("data").getString("testId");

                    // test status - wait until test complete or timeout exceeded
                    int iters = 0;
                    while (!statusText.equals("Test Complete") && iters*10 < timeoutInt) {
                        TimeUnit.SECONDS.sleep(10);
                        test = getResponseText("https://www.webpagetest.org/testStatus.php?" +
                                "f=json&test=" + testId);
                        test_json = new JSONObject(test);
                        statusText = test_json.getString("statusText");
                        iters += 1;
                    }

                    // timeout
                    if (!statusText.equals("Test Complete")) {
                        Toast.makeText(Page.this, "WebPageTest Timeout: "+test_url, Toast.LENGTH_LONG).show();
                        return;
                    }

                    // download test results csv - summary
                    //wv.loadUrl("https://www.webpagetest.org/result/"+testId+"/page_data.csv");

                    // download test results csv - details
                    wv.loadUrl("https://www.webpagetest.org/result/"+testId+"/requests.csv");

                }  catch (IOException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getResponseText(String stringUrl) throws IOException
    {
        StringBuilder response  = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
            String strLine = null;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }
}
