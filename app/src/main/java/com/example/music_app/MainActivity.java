package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    ProgressDialog pd;
    String receivedMusicList;
    String [] processedMusicList;
    ArrayAdapter<String> musicAdaptor;

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

    // get a new music list from server and try to add them into local music base
    public void parseReceivedMusicList(){
        for(String s: processedMusicList){
            Log.i("hahaha",s);
            switch (s) {
                case "a": {
                    try {
                        MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource("http://ec2-54-65-141-177.ap-northeast-1.compute.amazonaws.com:8080/music-repo/musics/spyFamily.mp3");
                        player.prepare();
                        addMusic_to_refer_noName("SpyFamily", "LYC", String.valueOf(player.getDuration()), R.drawable.arnya, R.raw.bird, player);
                        addItemToListView("SpyFamily");
                        break;
//                        player.start();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                case "b": {
                    MediaPlayer curMp = MediaPlayer.create(this, R.raw.danger);
                    addMusic_to_refer_noName(s, "LYC", String.valueOf(curMp.getDuration()), R.drawable.wind1, R.raw.danger, curMp);
                    addItemToListView(s);
                    break;
                }
                case "c": {
                    MediaPlayer curMp = MediaPlayer.create(this, R.raw.soft);
                    addMusic_to_refer_noName(s, "LYC", String.valueOf(curMp.getDuration()), R.drawable.storm2, R.raw.soft, curMp);
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


        try{
            String result = downloadTask.execute("http://tonghanghang.org/").get();
            Log.i("Result",result);
        }catch(Exception e){
            e.printStackTrace();
        }


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
        musicAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,Reference.musicNameList);
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
        Log.i("Info","In clickImage");
        ImageView my_image = (ImageView) findViewById(R.id.imageViewInfo);
        ImageDownloader task = new ImageDownloader();
        try {
            Bitmap myImage = task.execute("https://static.wikia.nocookie.net/simpsons/images/6/65/Bart_Simpson.png/revision/latest/scale-to-width-down/250?cb=20190409004756").get();
//            my_image.setImageBitmap(myImage);
        }catch(Exception e){
            e.printStackTrace();
        }


        // try to upload music online
//        new uploadMusic().execute();
        // try to download json online
        JsonTask j = new JsonTask();
        j.execute("http://ec2-54-65-141-177.ap-northeast-1.compute.amazonaws.com:8080/music-repo/users");

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




}