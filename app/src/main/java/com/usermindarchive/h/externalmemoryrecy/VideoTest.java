package com.usermindarchive.h.externalmemoryrecy;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoTest extends AppCompatActivity {

    VideoView vid;
    MediaController con;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);
        vid=(VideoView)findViewById(R.id.vid);
         con=new MediaController(this){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                AudioManager audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    Activity a = (Activity)getContext();
                    a.finish();
                }
                int action = event.getAction();
                int keyCode = event.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.KEYCODE_VOLUME_UP:
                        if (action == KeyEvent.ACTION_DOWN) {
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                        }
                        break;
                    case KeyEvent.KEYCODE_VOLUME_DOWN:
                        if (action == KeyEvent.ACTION_DOWN) {
                            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                        }
                        break;
                    default:
                        return super.dispatchKeyEvent(event);
                }
                return true;
            }
            @Override
            public void hide(){
                con.show(0);
            }
        };

        con.setAnchorView(vid);
        path=getIntent().getStringExtra("path");

        vid.setMediaController(con);
        Uri set=Uri.parse(path);
        if(path.endsWith(".mp3")){

            // setting the album image

            MediaMetadataRetriever pic = new MediaMetadataRetriever();
            pic.setDataSource(path);
            if(pic.getEmbeddedPicture()!=null){
                BitmapDrawable tr=new BitmapDrawable(BitmapFactory.decodeByteArray(pic.getEmbeddedPicture(),0,pic.getEmbeddedPicture().length));
                vid.setBackground(tr);

            }
            else{
                vid.setBackgroundResource(android.R.drawable.ic_media_play);
            }
        }
        vid.setVideoPath(path);
      //  vid.setVideoURI(set);


        vid.start();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            vid.stopPlayback();
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        vid.stopPlayback();
        finish();

    }
}
