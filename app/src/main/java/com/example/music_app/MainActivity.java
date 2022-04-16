package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;


// global variable class
class Reference{
    // global state variable
    public static int stopOrPlay = 1;
    public static int currentPlayMusic = 0;
    public static int musicNum = 0;
    public static int refreshFlag = 1;
    public static MediaPlayer cur_mp;
    public static AudioManager cur_am;

    // global list variable
    public static final ArrayList<String> musicNameList = new ArrayList<String>();
    public static final ArrayList<String> playerNameList = new ArrayList<String>();
    public static final ArrayList<String> durationNameList = new ArrayList<String>();
    public static ArrayList<MediaPlayer> mpList = new ArrayList<MediaPlayer>();
    public static ArrayList<Integer> musicIdList = new ArrayList<Integer>();
    public static ArrayList<Integer> imageIdList = new ArrayList<Integer>();

}


public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------tool functions----------------------------------------------------

    // set image and three textview in the main page
    public void setMusicInfo_to_mainPage(String musicName,String musicPlayer,String musicDuration,int musicImageId){
                 TextView NameView = findViewById(R.id.textViewMusicName);
                 TextView DurationView = findViewById(R.id.textViewDuration);
                 TextView PlayerView = findViewById(R.id.textViewMusicPlayer);
                 NameView.setText(musicName);
                 PlayerView.setText(musicPlayer);
                 DurationView.setText(musicDuration);
                 ImageView tempImageView = (ImageView) findViewById(R.id.imageViewInfo);
                 tempImageView.setImageResource(musicImageId);
    }

    // add a new music with basic info to the Reference class
    public void addMusic_to_refer(String name, String singer, String duration, int imageID, int musicID, MediaPlayer mp){
        Reference.musicNameList.add(name);
        Reference.durationNameList.add(duration);
        Reference.playerNameList.add(singer);
        Reference.imageIdList.add(imageID);
        Reference.musicIdList.add(musicID);
        Reference.mpList.add(mp);
        Reference.musicNum += 1;
    }

    // initialize the music database
    public void initialize_musicBase(){
           // load all music into the database
        MediaPlayer curMp = MediaPlayer.create(this,R.raw.hello);
        addMusic_to_refer("Luv Letter","ZZN","16:06",R.drawable.leave1,R.raw.hello,curMp);
        MediaPlayer curMp1 = MediaPlayer.create(this,R.raw.wind);
        addMusic_to_refer("Wind Song","LYC","00:09",R.drawable.wind1,R.raw.wind,curMp1);
        MediaPlayer curMp2 = MediaPlayer.create(this,R.raw.storm);
        addMusic_to_refer("Storm Song","ZLH","00:12",R.drawable.storm2,R.raw.storm,curMp2);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------------------------------------------------------Initialize MainPage------------------------------------------------------
        ListView myListView1 = findViewById(R.id.ListView1);
        ImageView imageViewInfo = (ImageView) findViewById(R.id.imageViewInfo);
        TextView textViewMusicName = findViewById(R.id.textViewMusicName);
        TextView textViewDuration = findViewById(R.id.textViewDuration);
        TextView textViewPlayer = findViewById(R.id.textViewMusicPlayer);

        // default mainPage text
        String default_musicName = "MusicName";
        String default_singer = "Singer";
        String default_duration = "Duration";

        textViewMusicName.setText(default_musicName);
        textViewPlayer.setText(default_singer);
        textViewDuration.setText(default_duration);

        // default mainPage image
        int m = R.drawable.bluetoe;
        imageViewInfo.setImageResource(m);

        // ------------------------------------------------------Initialize MusicBase-----------------------------------------------------
        initialize_musicBase();
        ArrayAdapter<String> musicAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,Reference.musicNameList);
        // setup ListView
        myListView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myListView1.setAdapter(musicAdaptor);

        // setup short click listener of listview
        myListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // default setting
                setMusicInfo_to_mainPage(default_musicName, default_singer, default_duration, Reference.imageIdList.get(0));
                if (i > (Reference.musicNum - 1) || i < 0) return;

                imageViewInfo.setImageResource(Reference.imageIdList.get(i));
                textViewPlayer.setText(Reference.playerNameList.get(i));
                textViewMusicName.setText(Reference.musicNameList.get(i));
                textViewDuration.setText(Reference.durationNameList.get(i));

            }
        });

        // setup long click listener of listview
        myListView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > (Reference.musicNum - 1) || i < 0){
                    Toast.makeText(getApplicationContext(),"This music is not available!",Toast.LENGTH_SHORT).show();
                    return false;
                }

                Toast.makeText(getApplicationContext(),"You are choosing: "+Reference.musicNameList.get(i),Toast.LENGTH_SHORT).show();
                Reference.currentPlayMusic = i;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MusicPlayerActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }
}