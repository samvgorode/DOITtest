package com.example.who.doittest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.who.doittest.R;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.who.doittest.global.Constants.TOKEN;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.ivAnimation)
    com.medialablk.easygifview.EasyGifView ivAnimation;

    private static final String EXIT = "EXIT";
    private Handler handler = new Handler();
    private boolean isExit = false;

    public static Intent getNewIntent(Context context, boolean isExit) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXIT, isExit);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setAnimation();
        fetchIntent();
    }

    private void setAnimation() {
        ivAnimation.setGifFromResource(R.raw.giphy);
    }

    private void fetchIntent() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = getIntent();
        if (intent.hasExtra(EXIT)) isExit = intent.getBooleanExtra(EXIT, false);
        if (isExit) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.finish();
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isExit) init();
    }

    private void init() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Hawk.contains(TOKEN)) startActivity(LoginActivity.getNewIntent(SplashActivity.this));
                else startActivity(GalleryActivity.getNewIntent(SplashActivity.this));
            }
        }, 3000);
    }
}
