package com.example.who.doittest.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.who.doittest.R;
import com.example.who.doittest.interfaces.IGalleryView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends AppCompatActivity implements IGalleryView{

    public static Intent getNewIntent(Context context) {
        Intent intent = new Intent(context, GalleryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setCustomBar();
    }

    private void setCustomBar() {
        if (getSupportActionBar() != null) {
            ActionBar bar = getSupportActionBar();
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setDisplayShowCustomEnabled(true);
            bar.setCustomView(R.layout.custom_bar);
            View view = bar.getCustomView();
            ImageView plus = (ImageView) view.findViewById(R.id.ivPlus);
            ImageView play = (ImageView) view.findViewById(R.id.ivDoGif);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNewImage();
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showGif();
                }
            });
        }
    }

    void addNewImage(){
        startActivity(AddImageActivity.getNewIntent(this));
    }

    void showGif(){

    }


}
