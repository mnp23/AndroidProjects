package com.zelo.prashanth.ydmapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {
    private static List<DataModel> dataSet;
    private Context context;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    static DownImgClickListenr itemClickListenr;

    public CustomAdapter(Context context,List<DataModel> data) {
        this.context = context;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        Picasso.with(context).load(R.drawable.picon1).into(holder.img_android);

        if(dataSet.get(i).isSelected())
        {

        }
    }
    public void setSelected(int pos) {
            try {
                if (dataSet.size() > 1) {
                    dataSet.get(mPref.getInt("position", 0)).setSelected(false);
                    mEditor.putInt("position", pos);
                    mEditor.commit();
                }
                dataSet.get(pos).setSelected(true);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnItemClickListener(CustomAdapter.DownImgClickListenr itemClickListenr) {
        this.itemClickListenr = (DownImgClickListenr) itemClickListenr;
    }

    public interface DownImgClickListenr {
        void onItemClick(int position, View v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_android,downl_icon;
        private static final String TAG = "Image OnClick";
        public MyViewHolder(View view) {
            super(view);
           img_android = (ImageView)view.findViewById(R.id.imageView);
           downl_icon = (ImageView)view.findViewById(R.id.dimg);

           downl_icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick " + getPosition() + " " + dataSet.toString());
            itemClickListenr.onItemClick(getAdapterPosition(),v);

        }

    }

}
