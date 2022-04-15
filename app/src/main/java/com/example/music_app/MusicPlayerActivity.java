package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

class Reference_MusicPlayer{
    public static int stopOrPlay = 1;
    public static int currentPlayMusic = 0;
}

public class MusicPlayerActivity extends AppCompatActivity{

    MediaPlayer mp1;
    MediaPlayer mp2;
    MediaPlayer mp3;
    AudioManager am1;
    AudioManager am2;
    AudioManager am3;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayeractivity_main);
        Button buttonReturn = findViewById(R.id.buttonReturn);

        // initialize images
        ImageView imageViewPlayStop = findViewById(R.id.imageViewPlayStop);
        imageViewPlayStop.setImageResource(R.drawable.play);
        ImageView imageViewNext = findViewById(R.id.imageViewNext);
        imageViewNext.setImageResource(R.drawable.next);
        ImageView imageViewPrevious = findViewById(R.id.imageViewPrevious);
        imageViewPrevious.setImageResource(R.drawable.previous);
        ImageView imageViewMain = findViewById(R.id.imageViewMusicPoster);
        if(Reference_MusicPlayer.currentPlayMusic == 0){
            imageViewMain.setImageResource(R.drawable.leave);
        }
        else if(Reference_MusicPlayer.currentPlayMusic == 1){
            imageViewMain.setImageResource(R.drawable.wind);

        }
        else if(Reference_MusicPlayer.currentPlayMusic == 2){
            imageViewMain.setImageResource(R.drawable.storm);
        }

        // setup music player
        mp1 = MediaPlayer.create(this,R.raw.hello);
        mp2 = MediaPlayer.create(this,R.raw.wind);
        mp3 = MediaPlayer.create(this,R.raw.storm);
        am1 = (AudioManager) getSystemService(AUDIO_SERVICE);
        am2 = (AudioManager) getSystemService(AUDIO_SERVICE);
        am3 = (AudioManager) getSystemService(AUDIO_SERVICE);
        int mv1 = am1.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int cv1 = am1.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mv2 = am2.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int cv2 = am2.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mv3 = am3.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int cv3 = am3.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar volumeControl = (SeekBar) findViewById(R.id.seekBarVolume);
        SeekBar progressControl = (SeekBar) findViewById(R.id.seekBarProgress);

        volumeControl.setMax(mv1);
        volumeControl.setProgress(cv1);

        if(Reference_MusicPlayer.currentPlayMusic == 0){
            progressControl.setMax(mp1.getDuration());
        }
        else if(Reference_MusicPlayer.currentPlayMusic == 1){
            progressControl.setMax(mp2.getDuration());

        }
        else if(Reference_MusicPlayer.currentPlayMusic == 2){
            progressControl.setMax(mp3.getDuration());
        }

        // setup listener
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(Reference_MusicPlayer.currentPlayMusic == 0){
                    am1.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 1){
                    am2.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);

                }
                else if(Reference_MusicPlayer.currentPlayMusic == 2){
                    am3.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        progressControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(Reference_MusicPlayer.currentPlayMusic == 0){
                    mp1.seekTo(i);
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 1){
                    mp2.seekTo(i);
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 2){
                    mp3.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(Reference_MusicPlayer.currentPlayMusic == 0){
                    progressControl.setProgress(mp1.getCurrentPosition());
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 1){
                    progressControl.setProgress(mp2.getCurrentPosition());
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 2){
                    progressControl.setProgress(mp3.getCurrentPosition());
                }

            }
        },0,300);


    }

    public void click_return(View view){
        finish();
    }

    public void click_PlayStop(View view){
        if(Reference_MusicPlayer.stopOrPlay == 1){
            Reference_MusicPlayer.stopOrPlay = 0;
            ImageView a = (ImageView) view;
            a.setImageResource(R.drawable.stop);
            Log.i("Info","in first if");
            if(Reference_MusicPlayer.currentPlayMusic == 0){
                mp1.start();
            }
            else if(Reference_MusicPlayer.currentPlayMusic == 1){
                mp2.start();
            }
            else if(Reference_MusicPlayer.currentPlayMusic == 2){
                mp3.start();
            }

        }
        else if(Reference_MusicPlayer.stopOrPlay == 0){
            Reference_MusicPlayer.stopOrPlay = 1;
            ImageView a = (ImageView) view;
            a.setImageResource(R.drawable.play);
            Log.i("Info","in second if");
            if(Reference_MusicPlayer.currentPlayMusic == 0){
                mp1.pause();
            }
            else if(Reference_MusicPlayer.currentPlayMusic == 1){
                mp2.pause();
            }
            else if(Reference_MusicPlayer.currentPlayMusic == 2){
                mp3.pause();
            }
        }

    }


}
