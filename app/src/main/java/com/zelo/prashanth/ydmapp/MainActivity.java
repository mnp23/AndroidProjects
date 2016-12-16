package com.zelo.prashanth.ydmapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {


    public final static String android_image_urls[] = {
            "https://s23.postimg.org/wv09hzp6z/image.jpg",
            "https://s23.postimg.org/ok905s30b/image.jpg",
            "https://s27.postimg.org/6n1cpkvwz/image.jpg",
            "https://s28.postimg.org/c6ghi4ax9/image.jpg",
            "https://s30.postimg.org/67xr9eaa9/image.jpg",
            "https://s27.postimg.org/ohii17owj/image.jpg",
            "https://s23.postimg.org/tiseoyo63/image.jpg",
            "https://s24.postimg.org/3zxjddm11/image.jpg",
            "https://s27.postimg.org/4l9gkz76r/image.jpg",
            "https://s28.postimg.org/tkowp7o9p/d10.jpg"
    };
    private CustomAdapter adapter;
  private static List androidImgs;
    private String name="downloadedImg";
    private ImageView imgDisplay,downIcon,viewIcon;
    private final int RES_ERROR = R.drawable.error_orange;
    private final int RES_PLACEHOLDER = R.drawable.placeholder_grey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
         androidImgs = prepareData();
        adapter = new CustomAdapter(getApplicationContext(), androidImgs);
        recyclerView.setAdapter(adapter);

        ((CustomAdapter) adapter).setOnItemClickListener(new CustomAdapter.DownImgClickListenr() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    Log.i(getClass().getSimpleName(), "the img clicked is " + androidImgs.get(position).toString());

                    adapter.setSelected(position);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Image Url");
                    alertDialog.setMessage("Enter URL");

                    final EditText input = new EditText(MainActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setPositiveButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = input.getText().toString();

                                    ydm(url);

                                }
                            });

                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            });

    }

    private void ydm(String url) {
        imgDisplay = (ImageView) findViewById(R.id.imageView);
        imgDisplay.setImageResource(RES_PLACEHOLDER);
        viewIcon = (ImageView)findViewById(R.id.imgResult);
        downIcon = (ImageView)findViewById(R.id.dimg);
        downIcon.setVisibility(View.INVISIBLE );
        final TextView tvPercent = (TextView) findViewById(R.id.tvPercent);
        final ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbImageLoading);
        tvPercent.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        final BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(BasicImageDownloader.ImageError error) {
                Toast.makeText(MainActivity.this, "Error code " + error.getErrorCode() + ": " +
                        error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                imgDisplay.setImageResource(RES_ERROR);
                tvPercent.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onProgressChange(int percent) {
                pbLoading.setProgress(percent);
                tvPercent.setText(percent + "%");
            }

            @Override
            public void onComplete(final Bitmap result) {

                        /* save the image - I'm gonna use JPEG */
                final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
                        /* don't forget to include the extension into the file name */
                final File myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "downImg" + File.separator + name + "." + mFormat.name().toLowerCase());
                BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                    @Override
                    public void onBitmapSaved() {
                        Toast.makeText(MainActivity.this, "Image saved as: " + myImageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                        Toast.makeText(MainActivity.this, "Error code " + error.getErrorCode() + ": " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }, mFormat, false);

                tvPercent.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                viewIcon.setVisibility(View.VISIBLE);

              // imgDisplay.setImageBitmap(result);
              //  imgDisplay.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
                viewIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),FullScreenImg.class);
                        i.putExtra("img",result);
                        startActivity(i);
                    }
                });
            }
        });
        downloader.download(url, true);

    }




    private ArrayList prepareData(){

        ArrayList android_img = new ArrayList<>();
        for(int i=0;i<android_image_urls.length;i++){
            DataModel androidIm = new DataModel();
            androidIm.setAndroid_image_url(android_image_urls[i]);
            android_img.add(androidIm);
        }
        return android_img;
    }

}


