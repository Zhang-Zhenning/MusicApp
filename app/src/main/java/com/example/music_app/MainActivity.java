package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;


// global variable class
class Reference{
    public static final ArrayList<String> musicList = new ArrayList<String>();
    public static final ArrayList<String> playerList = new ArrayList<String>();
    public static final ArrayList<String> durationList = new ArrayList<String>();
    public static int musicNum = 0;
    public static int refreshFlag = 1;

    //ok

}


public class MainActivity extends AppCompatActivity {

    protected void setMusicInfo(TextView NameView, TextView PlayerView, TextView DurationView, String musicName,String musicPlayer,String musicDuration){
                 NameView.setText(musicName);
                 PlayerView.setText(musicPlayer);
                 DurationView.setText(musicDuration);
    }

    protected void addMusic(String name, String singer, String duration){
        Reference.musicList.add(name);
        Reference.durationList.add(duration);
        Reference.playerList.add(singer);
        Reference.musicNum += 1;

        // ok

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --------------------------listview setting-------------------------
        ListView myListView1 = findViewById(R.id.ListView1);
        ImageView imageViewInfo = (ImageView) findViewById(R.id.imageViewInfo);
        TextView textViewMusicName = findViewById(R.id.textViewMusicName);
        TextView textViewDuration = findViewById(R.id.textViewDuration);
        TextView textViewPlayer = findViewById(R.id.textViewMusicPlayer);

        String a = "MusicName";
        String b = "Singer";
        String c = "Duration";
        String d = "Play until the end of war";
        String e = "MMN";
        String f = "17:09";
        textViewMusicName.setText(a);
        textViewPlayer.setText(b);
        textViewDuration.setText(c);



        imageViewInfo.setImageResource(R.drawable.bluetoe);
//        imageViewInfo.setImageResource();


        // array of music
          if(Reference.refreshFlag == 1) {
              addMusic(new String("Leave"), new String("ZZN"), new String("00:12"));
              addMusic(new String("Wind"), new String("LYC"), new String("00:09"));
              addMusic(new String("Storm"), new String("ZLH"), new String("00:06"));
          }
//        Reference.musicList.add("Fire");
//        Reference.musicList.add("Duck");
//        Reference.musicList.add("Dog");

        ArrayAdapter<String> musicAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,Reference.musicList);
        // bind adapter to listview
        myListView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myListView1.setAdapter(musicAdaptor);
        // choice button behavior

        // click behaviour
//        myListView1.setOnHoverListener(new View.OnHoverListener() {
//            @Override
//            public boolean onHover(View view, MotionEvent motionEvent) {
//                return false;
//            }
//        }){
//
//        };
        myListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                view.setVisibility(View.GONE);
                setMusicInfo(textViewMusicName,textViewPlayer,textViewDuration,d,e,f);
                if(i==0){
                    imageViewInfo.setImageResource(R.drawable.leave1);
                    textViewPlayer.setText(Reference.playerList.get(i));
                    textViewMusicName.setText(Reference.musicList.get(i));
                    textViewDuration.setText(Reference.durationList.get(i));
                }
                if(i==1){
                    imageViewInfo.setImageResource(R.drawable.wind1);
                    textViewPlayer.setText(Reference.playerList.get(i));
                    textViewMusicName.setText(Reference.musicList.get(i));
                    textViewDuration.setText(Reference.durationList.get(i));
                }
                if(i==2){
                    imageViewInfo.setImageResource(R.drawable.storm2);
                    textViewPlayer.setText(Reference.playerList.get(i));
                    textViewMusicName.setText(Reference.musicList.get(i));
                    textViewDuration.setText(Reference.durationList.get(i));
                    //ok
                }
            }

        });

        myListView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"You are choosing "+Reference.musicList.get(i),Toast.LENGTH_SHORT).show();
                Reference_MusicPlayer.currentPlayMusic = i;
//                Reference.refreshFlag = 0;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MusicPlayerActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }
}