package com.example.who.doittest;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

/**
 * Created by who on 28.09.2017.
 */

public class DoItTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
    }
}
