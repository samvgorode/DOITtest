package com.example.who.doittest.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.who.doittest.R;
import com.example.who.doittest.pojo.ImagePojo;
import com.example.who.doittest.utils.LocationUtils;

import java.util.List;

/**
 * Created by who on 30.09.2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private Context mContext;
    private List<ImagePojo> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView weather, address;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            weather = (TextView) view.findViewById(R.id.weather);
            address = (TextView) view.findViewById(R.id.address);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public GalleryAdapter(Context mContext, List<ImagePojo> imagesList) {
        this.mContext = mContext;
        this.albumList = imagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ImagePojo image = albumList.get(position);
        holder.weather.setText(image.getParameters().getWeather());
        String address = LocationUtils.getAddress(mContext, image.getParameters().getLatitude(), image.getParameters().getLongitude());
        holder.address.setText(address);
        Glide.with(mContext).load(image.getSmallImagePath()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
