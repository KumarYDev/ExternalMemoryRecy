package com.usermindarchive.h.externalmemoryrecy;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

public class Image extends AppCompatActivity {
ImageView fillt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        fillt=(ImageView)findViewById(R.id.imageView);
        String path=getIntent().getStringExtra("path");
        File fil=new File(path);
        fillt.setImageBitmap(BitmapFactory.decodeFile(fil.getAbsolutePath()));


    }

}
