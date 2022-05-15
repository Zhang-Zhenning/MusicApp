package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;
import com.example.music_app.marqueueText;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;

import java.sql.Ref;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.regex.MatchResult;


public class InitialPage extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_page);
    }

    public void intoMusicList(View view){
        Intent intent = new Intent();
        intent.setClass(InitialPage.this,MainActivity.class);
        startActivity(intent);

    }


}
