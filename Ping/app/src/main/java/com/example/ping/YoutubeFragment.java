package com.example.ping;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class YoutubeFragment extends AppCompatActivity {

    // init GUI
    YouTubePlayerView youTubePlayerView;
    float duration = 0; // variable storing the total duration of video in seconds
    float videoLoaded = 0; // variable storing the amount of video loaded in seconds
    float currentTime = 0; // variable storing the current time in seconds
    int playerState = -2; // variable storing the current player state
    String playbackQuality = "unknown"; // variable storing the current playback mode
    String playerError = "no error"; // variable storing the error encountered while running the video

    Button firestoreButton;
    List<String> possibleResolutions = new ArrayList<>(Arrays.asList("hd1080", "hd720", "small", "medium", "large", "highres", "default"));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_fragment);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        firestoreButton = findViewById(R.id.youtubeFirestoreButton);

        // add listener to youtube player view
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {
                videoLoaded = v*duration;
            }

            @Override
            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {
                duration = v;
            }

            @Override
            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                switch (state){
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
                switch(quality){
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
                System.out.println(playbackQuality);
            }

            @Override
            public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError error) {
                switch (error){
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
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "5-xhddr0zWs";
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        // syncing the playing of video with main activity, once the activity stops the video will not play in background
        getLifecycle().addObserver(youTubePlayerView);

        // Click Firestore Button
        firestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFirestore();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    // Storing the resolved parameters in firestore
    void saveToFirestore(){
        // get firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String datetime = new SimpleDateFormat("yyMMddHHmmssss").format(new Date());
        long docId = Long.parseLong(datetime);

        // create a map for storing data to firestore
        HashMap<String, Object> data = new HashMap<>();

        // setting timestamp as the time in UTC
        data.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd/MM/uuuu h:mm:ss a xxx")));

        // cheking for error while running the video
        if(playerError.equals("no error")) {
            data.put("curr_resolution", playbackQuality);
            data.put("playback_mode", playerState);
            data.put("possible_resolutions", possibleResolutions);
            data.put("downloaded_video", videoLoaded);
            data.put("video_played", currentTime);
            data.put("buffer_size", videoLoaded - currentTime);
        }
        else{
            data.put("player_error", playerError);
        }

        // adding to firestore
        db.collection("youtube").document(String.valueOf(docId)).set(data);
    }
}
