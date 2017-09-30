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
import com.orhanobut.hawk.Hawk;

import butterknife.ButterKnife;

import static com.example.who.doittest.global.Constants.TOKEN;

public class GalleryActivity extends AppCompatActivity implements IGalleryView, View.OnClickListener {

    private View customBarView;

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
            bar.setCustomView(R.layout.custom_bar_gallery);
            customBarView = bar.getCustomView();
            setBarClickListeners();
        }
    }

    private void setBarClickListeners() {
        ImageView plus = (ImageView) customBarView.findViewById(R.id.ivPlus);
        ImageView play = (ImageView) customBarView.findViewById(R.id.ivDoGif);
        ImageView logout = (ImageView) customBarView.findViewById(R.id.ivLogOut);
        plus.setOnClickListener(this);
        play.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivPlus:
                addNewImage();
                break;

            case R.id.ivDoGif:
                showGif();
                break;

            case R.id.ivLogOut:
                doLogOut();
                break;

            default:
                break;
        }
    }

    void addNewImage() {
        startActivity(AddImageActivity.getNewIntent(this));
    }

    void showGif() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(SplashActivity.getNewIntent(this, true));
        finish();
    }

    private void doLogOut() {
        if(Hawk.contains(TOKEN)) Hawk.delete(TOKEN);
        startActivity(SplashActivity.getNewIntent(this, false));
        finish();
    }
}
