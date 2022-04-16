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
//    public static MediaPlayer cur_mp;
}

public class MusicPlayerActivity extends AppCompatActivity{

    MediaPlayer cur_mp;
    AudioManager cur_am;
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
            cur_mp = mp1;
            cur_am = am1;
        }
        else if(Reference_MusicPlayer.currentPlayMusic == 1){
            progressControl.setMax(mp2.getDuration());
            cur_mp = mp2;
            cur_am = am2;

        }
        else if(Reference_MusicPlayer.currentPlayMusic == 2){
            progressControl.setMax(mp3.getDuration());
            cur_mp = mp3;
            cur_am = am3;
        }

        // setup listener
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(Reference_MusicPlayer.currentPlayMusic == 0){
                    cur_am = am1;
                    cur_am.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 1){
                    cur_am = am2;
                    cur_am.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);

                }
                else if(Reference_MusicPlayer.currentPlayMusic == 2){
                    cur_am = am3;
                    cur_am.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
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
                    progressControl.setMax(mp1.getDuration());
                    mp1.seekTo(i);
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 1){
                    progressControl.setMax(mp2.getDuration());
                    mp2.seekTo(i);
                }
                else if(Reference_MusicPlayer.currentPlayMusic == 2){
                    progressControl.setMax(mp3.getDuration());
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

    public void click_next(View view){
                int previous_music = Reference_MusicPlayer.currentPlayMusic;
                Reference_MusicPlayer.currentPlayMusic = (Reference_MusicPlayer.currentPlayMusic + 1) % 3;
                // reset picture
                ImageView imageViewMain1 = findViewById(R.id.imageViewMusicPoster);
                if(Reference_MusicPlayer.currentPlayMusic == 0){
                    imageViewMain1.setImageResource(R.drawable.leave);
                    SeekBar progressControl1 = (SeekBar) findViewById(R.id.seekBarProgress);
                    progressControl1.setMax(mp1.getDuration());

                }
                else if(Reference_MusicPlayer.currentPlayMusic == 1){
                    imageViewMain1.setImageResource(R.drawable.wind);
                    SeekBar progressControl1 = (SeekBar) findViewById(R.id.seekBarProgress);
                    progressControl1.setMax(mp2.getDuration());

                }
                else if(Reference_MusicPlayer.currentPlayMusic == 2){
                    imageViewMain1.setImageResource(R.drawable.storm);
                    SeekBar progressControl1 = (SeekBar) findViewById(R.id.seekBarProgress);
                    progressControl1.setMax(mp3.getDuration());
                }

                if(Reference_MusicPlayer.stopOrPlay == 0){
                    // now it is playing
                    if(previous_music == 0){

                        mp1.pause();
                        mp1.seekTo(0);

                    }
                    else if(previous_music == 1){

                        mp2.pause();
                        mp2.seekTo(0);

                    }
                    else if(previous_music == 2){

                        mp3.pause();
                        mp3.seekTo(0);

                    }

                    // start new music
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
                else{
                    // it is not playing
                    if(previous_music == 0){

//                        mp1.pause();
                        mp1.seekTo(0);

                    }
                    else if(previous_music == 1){

//                        mp2.pause();
                        mp2.seekTo(0);

                    }
                    else if(previous_music == 2){

//                        mp3.pause();
                        mp3.seekTo(0);

                    }

                }


    }

    public void click_previous(View view){
        int previous_music = Reference_MusicPlayer.currentPlayMusic;
//        Reference_MusicPlayer.currentPlayMusic = (Reference_MusicPlayer.currentPlayMusic - 1) % 3;
        if(Reference_MusicPlayer.currentPlayMusic == 0){
            Reference_MusicPlayer.currentPlayMusic = 2;
        }
        else if(Reference_MusicPlayer.currentPlayMusic == 1){
            Reference_MusicPlayer.currentPlayMusic = 0;

        }
        else if(Reference_MusicPlayer.currentPlayMusic == 2){
            Reference_MusicPlayer.currentPlayMusic = 1;
        }
        // reset picture
        ImageView imageViewMain1 = findViewById(R.id.imageViewMusicPoster);
        if(Reference_MusicPlayer.currentPlayMusic == 0){
            imageViewMain1.setImageResource(R.drawable.leave);
        }
        else if(Reference_MusicPlayer.currentPlayMusic == 1){
            imageViewMain1.setImageResource(R.drawable.wind);

        }
        else if(Reference_MusicPlayer.currentPlayMusic == 2){
            imageViewMain1.setImageResource(R.drawable.storm);
        }

        if(Reference_MusicPlayer.stopOrPlay == 0){
            // now it is playing
            if(previous_music == 0){

                mp1.pause();
                mp1.seekTo(0);

            }
            else if(previous_music == 1){

                mp2.pause();
                mp2.seekTo(0);

            }
            else if(previous_music == 2){

                mp3.pause();
                mp3.seekTo(0);

            }

            // start new music
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
        else{
            // it is not playing
            if(previous_music == 0){

//                        mp1.pause();
                mp1.seekTo(0);

            }
            else if(previous_music == 1){

//                        mp2.pause();
                mp2.seekTo(0);

            }
            else if(previous_music == 2){

//                        mp3.pause();
                mp3.seekTo(0);

            }

        }


    }

    public void click_PlayStop(View view){
        if(Reference_MusicPlayer.stopOrPlay == 1){
            Reference_MusicPlayer.stopOrPlay = 0;
            ImageView a = (ImageView) view;
            a.setImageResource(R.drawable.stop);
            Log.i("Info","in first if");
//            cur_mp.start();
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
//            cur_mp.pause();
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
