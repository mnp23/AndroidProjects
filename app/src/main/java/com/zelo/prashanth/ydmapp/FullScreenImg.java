package com.zelo.prashanth.ydmapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;


public class FullScreenImg extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.fullimage);
        Intent fromIntent = getIntent();
        Bitmap bitmap = (Bitmap) fromIntent.getParcelableExtra("img");
        ImageView imageView = (ImageView) findViewById(R.id.fullImg);
        imageView.setImageBitmap(bitmap);
    }
}
