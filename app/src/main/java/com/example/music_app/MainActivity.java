package com.example.music_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.music_app.marqueueText;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


// global variable class
class Reference{
    // global state variable
    public static int stopOrPlay = 1;
    public static int currentPlayMusic = 0;
    public static int musicNum = 0;
    public static int refreshFlag = 1;
    public static MediaPlayer cur_mp;
    public static AudioManager cur_am;
    public static int heartModeOn = 0;
    public static int heartModeFinish = 1;

    // global list variable
    public static final ArrayList<String> musicNameList = new ArrayList<String>();
    public static final ArrayList<String> playerNameList = new ArrayList<String>();
    public static final ArrayList<String> durationNameList = new ArrayList<String>();
    public static ArrayList<MediaPlayer> mpList = new ArrayList<MediaPlayer>();
    public static ArrayList<Integer> musicIdList = new ArrayList<Integer>();
    public static ArrayList<Integer> imageIdList = new ArrayList<Integer>();
    public static ObjectAnimator anim;
}






public class MainActivity extends AppCompatActivity {

    ProgressDialog pd;
    String receivedMusicList;
    String [] processedMusicList;
    ArrayAdapter<String> musicAdaptor;
    String musicListURL = "http://ec2-54-65-141-177.ap-northeast-1.compute.amazonaws.com:8080/music-repo/musics/";

    // the recommend music list from last time!
    ArrayList<String> lastNames = new ArrayList<String>();
    ArrayList<String> lastSingers = new ArrayList<String>();

    // for downloading from internet
    public class DownloadTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings){
            Log.i("URL",strings[0]);
            String result_in = "";
            URL url = null;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);

                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                }
                 catch (IOException e) {
                    e.printStackTrace();
                    return "Failed";
                }


                InputStream inputS = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputS);
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result_in += (current);
                    data = reader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed";
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("Result",result_in);
            return result_in;
        }
    }

    // --------------------------------------------------tool functions----------------------------------------------------

    // based

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

    // when updating the UI list, we can not add name to namelist twice!
    public void addMusic_to_refer_noName(String name, String singer, String duration, int imageID, int musicID, MediaPlayer mp){
//        Reference.musicNameList.add(name);
        Reference.durationNameList.add(duration);
        Reference.playerNameList.add(singer);
        Reference.imageIdList.add(imageID);
        Reference.musicIdList.add(musicID);
        Reference.mpList.add(mp);
        Reference.musicNum += 1;
    }

    public String finetuneDuration(int miliDuration){
        int secDuration = miliDuration / 1000;
        int min = secDuration / 60;
        int sec = secDuration % 60;

        String secStr = String.valueOf(sec);
        if (secStr.length() == 1){
            secStr = secStr + "0";
        }
        return String.valueOf(min) + ":" + secStr;
    }

    // meaningless now
    public void parseReceivedMusicList(){
        for(String s: processedMusicList){
            Log.i("hahaha",s);
            switch (s) {
                case "a": {
                    try {
                        MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource("http://ec2-54-65-141-177.ap-northeast-1.compute.amazonaws.com:8080/music-repo/musics/spyFamily.mp3");
                        player.prepareAsync();
                        addMusic_to_refer_noName("SpyFamily", "LYC", finetuneDuration(player.getDuration()), R.drawable.arnya, R.raw.bird, player);
                        addItemToListView("SpyFamily");
                        break;
//                        player.start();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                case "b": {
                    MediaPlayer curMp = MediaPlayer.create(this, R.raw.danger);
                    addMusic_to_refer_noName(s, "LYC", finetuneDuration(curMp.getDuration()), R.drawable.wind1, R.raw.danger, curMp);
                    addItemToListView(s);
                    break;
                }
                case "c": {
                    MediaPlayer curMp = MediaPlayer.create(this, R.raw.soft);
                    addMusic_to_refer_noName(s, "LYC", finetuneDuration(curMp.getDuration()), R.drawable.storm2, R.raw.soft, curMp);
                    addItemToListView(s);

                    break;
                }
            }
        }

        // refresh the musiclist in the main page
//        processedMusicList = new String[] {};

    }

    // initialize the music database
    public void initialize_musicBase(){
           // load all music into the database
        MediaPlayer curMp = MediaPlayer.create(this,R.raw.hello);
        addMusic_to_refer("Luv Letter","ZZN","16:06",R.drawable.leave1,R.raw.hello,curMp);
        MediaPlayer curMp1 = MediaPlayer.create(this,R.raw.wind);
        addMusic_to_refer("Wind Song","XPY","00:09",R.drawable.wind1,R.raw.wind,curMp1);
        MediaPlayer curMp2 = MediaPlayer.create(this,R.raw.storm);
        addMusic_to_refer("Storm Song","ZLH","00:12",R.drawable.storm2,R.raw.storm,curMp2);



    }

    public void addItemToListView(final String value){
        runOnUiThread(new Runnable() {
            @Override

            public void run() {
                Log.i("fuck",value);
                musicAdaptor.add(value);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // download thread
        DownloadTask downloadTask = new DownloadTask();




//        try{
//            String result = downloadTask.execute("http://tonghanghang.org/").get();
//            Log.i("Result",result);
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i("test","timer");
                new downloadMusicList().execute(musicListURL);

            }
        },0,300);


        // ------------------------------------------------------Initialize MainPage------------------------------------------------------
        ListView myListView1 = findViewById(R.id.ListView1);
        ImageView imageViewInfo = (ImageView) findViewById(R.id.imageViewInfo);
        marqueueText textViewMusicName = findViewById(R.id.textViewMusicName);
        marqueueText textViewDuration = findViewById(R.id.textViewDuration);
        marqueueText textViewPlayer = findViewById(R.id.textViewMusicPlayer);

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
        // customized arrayadapter
        musicAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,Reference.musicNameList){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the typeface/font for the current item
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Typeface typeface = getResources().getFont(R.font.newyear);
                    item.setTypeface(typeface);

                    // Set the list view item's text color


                    // Change the item text size
                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,21);
                }


                // return the view
                return item;
            }
        };


        // setup ListView
        myListView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myListView1.setAdapter(musicAdaptor);

        // setup short click listener of listview
        myListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // default setting
//                setMusicInfo_to_mainPage(default_musicName, default_singer, default_duration, Reference.imageIdList.get(0));
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

    public void click_image(View view){

        if(Reference.heartModeOn == 1) {
            Reference.heartModeOn = 0;
            Button t = findViewById(R.id.buttonDownload);
            t.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        else {
            Reference.heartModeOn = 1;
            Button t = findViewById(R.id.buttonDownload);
            t.setBackgroundColor(getResources().getColor(R.color.purple_200));
        }

//        Log.i("Info","In clickImage");
//        ImageView my_image = (ImageView) findViewById(R.id.imageViewInfo);
//        ImageDownloader task = new ImageDownloader();
//        try {
//            Bitmap myImage = task.execute("https://static.wikia.nocookie.net/simpsons/images/6/65/Bart_Simpson.png/revision/latest/scale-to-width-down/250?cb=20190409004756").get();
////            my_image.setImageBitmap(myImage);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//
//        // try to upload music online
////        new uploadMusic().execute();
//        // try to download json online
//        JsonTask j = new JsonTask();
//        j.execute("http://ec2-54-65-141-177.ap-northeast-1.compute.amazonaws.com:8080/music-repo/users");

    }

    public class ImageDownloader extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... urls){
            try {
                URL url = new URL(urls[0]);
                try{
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    return BitmapFactory.decodeStream(in);

                }catch(IOException e){
                    e.printStackTrace();
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }



        }
    }

    public class uploadMusic extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String existingFileName =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/a.mp3";
            ;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            String responseFromServer = "";
            String urlString = "http://ec2-18-183-81-64.ap-northeast-1.compute.amazonaws.com:8080/music-repo/music";

            try {

                //------------------ CLIENT REQUEST
                File file=new File(existingFileName);
                FileInputStream fileInputStream = new FileInputStream(file);
                // open a URL connection to the Servlet
                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // close streams
                Log.e("Debug", "File is written");
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (IOException ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }

            return null;
        }
    }

    // down load a json
    private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
//                TextView a = findViewById(R.id.textView2);
//                a.setText(buffer.toString());
//                Log.i("receiving",buffer.toString());
//                receivedMusicList = buffer.toString();
//                receivedMusicList = receivedMusicList.substring(1, receivedMusicList.length()-2);
//                processedMusicList = receivedMusicList.split(",");
//                for (String s: processedMusicList) {
//                    //Do your stuff here
//                    Log.i("processing",s);
//                }
                processedMusicList = new String[] {"a","b","c"};

                parseReceivedMusicList();

                return buffer.toString();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


    }

    // down load a json
    private class downloadMusicList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            if(Reference.heartModeOn == 0 || Reference.heartModeFinish == 0) return "Skip";

            Reference.heartModeFinish = 0;

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                String rawData = buffer.toString();
                rawData = rawData.substring(1,rawData.length()-2);
                String interval = ",";
                String[] splitData = rawData.split(interval);

                ArrayList<String> tempNames = new ArrayList<String>();
                ArrayList<String> tempSingers = new ArrayList<String>();
                ArrayList<MediaPlayer> tempMpList = new ArrayList<MediaPlayer>();

                ArrayList<String> newLastNames = new ArrayList<String>();
                ArrayList<String> newLastSingers = new ArrayList<String>();

                // parse the music list
                for (String s : splitData){
//                    Log.i("jsonTest",s.substring(1,7));

                    if(s.substring(2,6).equals("name") ){
                        tempNames.add(s.substring(9,s.length()-1-4));
//                        Log.i("test",s.substring(9,s.length()-1));
                    }
                    else if(s.substring(1,7).equals("author")){
                        tempSingers.add(s.substring(10,s.length()-2));
//                        Log.i("test",s.substring(10,s.length()-2));
                    }
                }

                int flag = 0;
//
                // download the music
                for(int i = 0; i < tempNames.size();i++){
                    try {

                        if(lastNames.contains(tempNames.get(i)) && lastSingers.contains(tempSingers.get(i))){
                            continue;
                        }

                        flag = 1;
                        newLastNames.add(tempNames.get(i));
                        newLastSingers.add(tempSingers.get(i));

                        MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(musicListURL+tempNames.get(i)+".WAV");
                        Log.i("NewMusic",musicListURL+tempNames.get(i)+".WAV");
                        player.prepare();
//                        player.start();
                        addMusic_to_refer_noName(tempNames.get(i), tempSingers.get(i), finetuneDuration(player.getDuration()), R.drawable.arnya, R.raw.bird, player);
                        addItemToListView(tempNames.get(i));

//                        player.start();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                if (flag == 0){
                    Reference.heartModeFinish = 1;
                    return buffer.toString();
                }

                lastSingers = newLastSingers;
                lastNames = newLastNames;
                Reference.heartModeFinish = 1;


                return buffer.toString();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


    }




}