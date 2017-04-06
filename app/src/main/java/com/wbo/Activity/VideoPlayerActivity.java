package com.wbo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wbo.R;

/**
 * Created by 丶 on 2017/4/6.
 */

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        videoView= (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }
}
