package com.example.ping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class YoutubeFragment extends AppCompatActivity {
    // init GUI
    RecyclerView recyclerView;
    YoutubeAdapter adapter;
    Button exportButton, clearButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_fragment);
        recyclerView = findViewById(R.id.youtube_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(YoutubeFragment.this) {

                    private static final float SPEED = 100000f;

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }

                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };

        adapter = new YoutubeAdapter(this.getLifecycle(), this);
        adapter.setHasStableIds(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();

        exportButton = findViewById(R.id.youtube_export_button);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });

        clearButton = findViewById(R.id.youtube_clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
    }

    // Export text file
    public void export() {
        try {
            // file names for various files containing dns records
            String fileName = "Youtube_Data";

            // init file locations and paths
            File fileLocation = new File(this.getFilesDir() + "/" + fileName);
            Uri path = FileProvider.getUriForFile(this, "com.example.Ping.fileprovider", fileLocation);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/txt");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Youtube_Data");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(intent, "Export Youtube Data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // clear text file
    void clear() {
        String fileName = "Youtube_Data";
        this.deleteFile(fileName);
        Toast.makeText(this, "File cleared", Toast.LENGTH_SHORT).show();
    }
}
