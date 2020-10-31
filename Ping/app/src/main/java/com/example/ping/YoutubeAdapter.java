package com.example.ping;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.YoutubeViewHolder> {
    private static final String[] videoId = new String[]{"eWI_BtcDJu0", "QrVAGD8l5YU", "iIovgwl5-aY", "58bRNmxUSUg", "165VjNKRNdw", "kZl97ec_97s", "kNrJhb1iv8U", "Y8u42OjH0ss", "riXYBfpwCek", "QwCOLuecsWs"};
    private final Lifecycle lifecycle;
    private final Context context;

    YoutubeAdapter(Lifecycle lifecycle, Context context) {
        this.lifecycle = lifecycle;
        this.context = context;
    }

    class YoutubeViewHolder extends RecyclerView.ViewHolder {
        // init GUI
        private YouTubePlayerView youTubePlayerView;
        private Button button;

        private String videoId; // variable containing the video id
        private YouTubePlayer youTubePlayer; // variable storing the youtube player for the video
        private float duration = 0; // variable storing the total duration of video in seconds
        private float videoLoaded = 0; // variable storing the amount of video loaded in seconds
        private float currentTime = 0; // variable storing the current time in seconds
        private int playerState = -2; // variable storing the current player state
        private String playbackQuality = "unknown"; // variable storing the current resolution
        private String playerError = "no error"; // variable storing the error encountered while running the video
        long seconds = 0; // variable storing the time interval for which to take measurements

        public YoutubeViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
            button = itemView.findViewById(R.id.youtubeButton);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {
                    videoLoaded = v * duration;
                }

                @Override
                public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {
                    duration = v;
                    Task task = new Task();
                    task.execute();
                }

                @Override
                public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                    switch (state) {
                        case UNSTARTED:
                            playerState = -1;
                            break;
                        case ENDED:
                            playerState = 0;
                            break;
                        case PLAYING:
                            playerState = 1;
                            break;
                        case PAUSED:
                            playerState = 2;
                            break;
                        case BUFFERING:
                            playerState = 3;
                            break;
                        case VIDEO_CUED:
                            playerState = 5;
                            break;
                        default:
                            playerState = -2;
                            break;
                    }
                }

                @Override
                public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality quality) {
                    switch (quality) {
                        case HD720:
                            playbackQuality = "hd720";
                            break;
                        case HD1080:
                            playbackQuality = "hd1080";
                            break;
                        case HIGH_RES:
                            playbackQuality = "highres";
                            break;
                        case DEFAULT:
                            playbackQuality = "default";
                            break;
                        case SMALL:
                            playbackQuality = "small";
                            break;
                        case MEDIUM:
                            playbackQuality = "medium";
                            break;
                        case LARGE:
                            playbackQuality = "large";
                            break;
                        default:
                            playbackQuality = "unknown";
                            break;
                    }
                }

                @Override
                public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError error) {
                    switch (error) {
                        case INVALID_PARAMETER_IN_REQUEST:
                            playerError = "invalid_paramter_in_request";
                            break;
                        case HTML_5_PLAYER:
                            playerError = "html_5_player";
                            break;
                        case VIDEO_NOT_FOUND:
                            playerError = "video_not_found";
                            break;
                        case VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER:
                            playerError = "video_not_playable_in_embedded_player";
                            break;
                        default:
                            playerError = "no error";
                            break;
                    }
                }

                @Override
                public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {
                    currentTime = v;
                }

                @Override
                public void onReady(@NonNull YouTubePlayer initializeyouTubePlayer) {
                    youTubePlayer = initializeyouTubePlayer;
                    youTubePlayer.cueVideo(videoId, 0);
                }
            });

            // syncing the playing of video with main activity, once the activity stops the video will not play in background
            lifecycle.addObserver(youTubePlayerView);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // setting up alert dialog box which will pop up and ask for time interval of measurements
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Enter Seconds");

                    TextInputLayout textInputLayout = new TextInputLayout(context);
                    final EditText input = new EditText(context);

                    FrameLayout container = new FrameLayout(context);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    input.setInputType(InputType.TYPE_CLASS_NUMBER);

                    int left_margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
                    ;
                    int top_margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
                    ;
                    int right_margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
                    ;
                    int bottom_margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
                    ;
                    params.setMargins(left_margin, top_margin, right_margin, bottom_margin);
                    textInputLayout.setLayoutParams(params);

                    textInputLayout.addView(input);
                    container.addView(textInputLayout);

                    builder.setView(container);

                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            seconds = Integer.parseInt(input.getText().toString());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }

        // additional method ensuring to cue video with youtube player
        void cueVideo(String currentVideoId) {
            videoId = currentVideoId;
            if (youTubePlayer == null) {
                return;
            }
            youTubePlayer.cueVideo(videoId, 0);
        }

        // async task to perform the measurements in the background
        private class Task extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                String datetime = new SimpleDateFormat("yyMMddHHmmssss").format(new Date());
                long docId = Long.parseLong(datetime);
                for (int i = 0; i < seconds; i++) {
                    try {
                        saveToFirestore(String.valueOf(docId));
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(context, "Processing Complete", Toast.LENGTH_SHORT).show();
            }
        }

        // Storing the resolved parameters in firestore
        void saveToFirestore(String docSerID) {
            // get firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String localPlaybackQuality = playbackQuality;
            int localPlayerState = playerState;
            float localVideoLoaded = videoLoaded;
            float localCurrentTime = currentTime;
            float localBufferSize = videoLoaded - currentTime;
            String localPlayerError = playerError;

            String datetime = new SimpleDateFormat("yyMMddHHmmssss").format(new Date());
            long docId = Long.parseLong(datetime);

            HashMap<String, Object> dataNew = new HashMap<>();
            dataNew.put("video_id", videoId);
            dataNew.put("time_interval", seconds);

            // adding to firestore
            db.collection("youtube").document(docSerID).set(dataNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                    // create a map for storing data to firestore
                    HashMap<String, Object> data = new HashMap<>();

                    // setting timestamp as the time in UTC
                    data.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd/MM/uuuu h:mm:ss a xxx")));

                    // cheking for error while running the video
                    if (localPlayerError.equals("no error")) {
                        data.put("curr_resolution", localPlaybackQuality);
                        data.put("playback_mode", localPlayerState);
                        data.put("downloaded_video", localVideoLoaded);
                        data.put("video_played", localCurrentTime);
                        data.put("buffer_size", localBufferSize);
                    } else {
                        data.put("player_error", playerError);
                    }
                    db.collection("youtube").document(docSerID).collection("metric").document(String.valueOf(docId)).set(data);
                }
            });

            // write the data to file
            write(videoId, String.valueOf(docId), localPlaybackQuality, localPlayerState, localVideoLoaded, localCurrentTime, localBufferSize);
        }

        // Write to text file
        public void write(String videoId, String timestamp, String curr_resolution, int playback_mode, float downloaded_video, float video_loaded, float buffer_size) {
            String header;
            FileOutputStream fileOutputStream = null;
            String fileName = "Youtube_Data";
            boolean initRecord = false;

            // init file locations
            File fileLocation = new File(context.getFilesDir() + "/" + fileName);

            // add header if file is empty
            if (fileLocation.length() == 0) {
                header = "video_url | timestamp | curr_resolution | playback_mode | downloaded_video | video_played | buffer_size\n\n";
            } else {
                header = "\n";
            }
            // Initialize file if one does not exist
            if (!fileLocation.exists()) {
                initRecord = true;
            }

            try {
                fileOutputStream = new FileOutputStream(fileLocation, true);
                if (initRecord) {
                    fileOutputStream.write(header.getBytes());
                }
                fileOutputStream.write((videoId + "|").getBytes());
                fileOutputStream.write((timestamp + "|").getBytes());
                fileOutputStream.write((curr_resolution + "|").getBytes());
                fileOutputStream.write((playback_mode + "|").getBytes());
                fileOutputStream.write((downloaded_video + "|").getBytes());
                fileOutputStream.write((video_loaded + "|").getBytes());
                fileOutputStream.write(String.valueOf(buffer_size).getBytes());
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("WRITE", "Error Writing file");
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

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_youtube_item, parent, false);
        return new YoutubeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeViewHolder holder, int position) {
        holder.cueVideo(videoId[position]);
    }

    @Override
    public int getItemCount() {
        return videoId.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
