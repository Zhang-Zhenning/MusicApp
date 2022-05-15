package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;
import com.example.music_app.marqueueText;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.sql.Ref;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.regex.MatchResult;


public class MusicPlayerActivity extends AppCompatActivity{

    AudioManager cur_am;


    // when we enter this page for the first time, need to initialize it firstly
    public void initialize_musicPlayer(){

        // play/puase, next/previous button initialization
        ImageView imageViewPlayStop = findViewById(R.id.imageViewPlayStop);
        imageViewPlayStop.setImageResource(R.drawable.play);
        ImageView imageViewNext = findViewById(R.id.imageViewNext);
        imageViewNext.setImageResource(R.drawable.next);
        ImageView imageViewPrevious = findViewById(R.id.imageViewPrevious);
        imageViewPrevious.setImageResource(R.drawable.previous);

        // music poster image initialization
        ImageView imageViewMain = findViewById(R.id.imageViewMusicPoster);
        imageViewMain.setImageResource(Reference.imageIdList.get(Reference.currentPlayMusic));
        marqueueText title = findViewById(R.id.textViewTitle);
        title.setText(Reference.musicNameList.get(Reference.currentPlayMusic));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayeractivity_main);
        Button buttonReturn = findViewById(R.id.buttonReturn);

        // -----------------------------------initialize music player UI------------------------------------
        initialize_musicPlayer();

        // ---------------------------------------setup music player----------------------------------------
        // define two seekbars
        SeekBar volumeControlBar = (SeekBar) findViewById(R.id.seekBarVolume);
        SeekBar progressControlBar = (SeekBar) findViewById(R.id.seekBarProgress);
        // setup audio manager which we only need one
        cur_am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int max_volume = cur_am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current_volume = cur_am.getStreamVolume(AudioManager.STREAM_MUSIC);
        // initialize volumeControlBar
        volumeControlBar.setMax(max_volume);
        volumeControlBar.setProgress(current_volume);
        // initialize progressControlBar
        progressControlBar.setMax(Reference.mpList.get(Reference.currentPlayMusic).getDuration());


        // setup listener for volume control bar
        volumeControlBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cur_am.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // setup listener for progress control bar
        progressControlBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressControlBar.setMax(Reference.mpList.get(Reference.currentPlayMusic).getDuration());
                Reference.mpList.get(Reference.currentPlayMusic).seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // setup a timer to refresh the bar regularly
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                progressControlBar.setProgress(Reference.mpList.get(Reference.currentPlayMusic).getCurrentPosition());
            }
        },0,300);

    }

    // after click the return button, return to the parent page
    public void click_return(View view){
        finish();
    }


    public void click_next(View view){
                int previous_music = Reference.currentPlayMusic;
                Reference.currentPlayMusic = (Reference.currentPlayMusic + 1) % Reference.musicNum;

                // reset picture
                ImageView imageViewMain1 = findViewById(R.id.imageViewMusicPoster);
                imageViewMain1.setImageResource(Reference.imageIdList.get(Reference.currentPlayMusic));
                marqueueText title = findViewById(R.id.textViewTitle);
                title.setText(Reference.musicNameList.get(Reference.currentPlayMusic));
                SeekBar progressControlBar1 = (SeekBar) findViewById(R.id.seekBarProgress);
                progressControlBar1.setMax(Reference.mpList.get(Reference.currentPlayMusic).getDuration());



                if(Reference.stopOrPlay == 0){
                    // now it is playing
                    // stop the previous one, clear the progress
                    Reference.mpList.get(previous_music).pause();
                    Reference.mpList.get(previous_music).seekTo(0);
                    // start the new one
                    Reference.mpList.get(Reference.currentPlayMusic).start();
                }
                else{
                    // it is not playing
                    // just clear the progress
                    Reference.mpList.get(previous_music).seekTo(0);
                }

    }

    public void click_previous(View view){
        int previous_music = Reference.currentPlayMusic;

        if(Reference.currentPlayMusic == 0){
            Reference.currentPlayMusic = Reference.musicNum - 1;
        }
        else {
            Reference.currentPlayMusic = (Reference.currentPlayMusic - 1) % Reference.musicNum;
        }

        // reset picture
        ImageView imageViewMain1 = findViewById(R.id.imageViewMusicPoster);
        imageViewMain1.setImageResource(Reference.imageIdList.get(Reference.currentPlayMusic));
        marqueueText title = findViewById(R.id.textViewTitle);
        title.setText(Reference.musicNameList.get(Reference.currentPlayMusic));
        SeekBar progressControlBar1 = (SeekBar) findViewById(R.id.seekBarProgress);
        progressControlBar1.setMax(Reference.mpList.get(Reference.currentPlayMusic).getDuration());



        if(Reference.stopOrPlay == 0){
            // now it is playing
            // stop the previous one, clear the progress
            Reference.mpList.get(previous_music).pause();
            Reference.mpList.get(previous_music).seekTo(0);
            // start the new one
            Reference.mpList.get(Reference.currentPlayMusic).start();
        }
        else{
            // it is not playing
            // just clear the progress
            Reference.mpList.get(previous_music).seekTo(0);
        }

    }


    public void click_PlayStop(View view){
        if(Reference.stopOrPlay == 1){
            Reference.stopOrPlay = 0;
            ImageView a = (ImageView) view;
            a.setImageResource(R.drawable.stop);
            Log.i("Info","in first if");
            Reference.mpList.get(Reference.currentPlayMusic).start();

        }
        else if(Reference.stopOrPlay == 0){
            Reference.stopOrPlay = 1;
            ImageView a = (ImageView) view;
            a.setImageResource(R.drawable.play);
            Log.i("Info","in second if");
            Reference.mpList.get(Reference.currentPlayMusic).pause();
        }

    }


}
