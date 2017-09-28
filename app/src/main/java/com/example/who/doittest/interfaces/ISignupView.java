package com.example.who.doittest.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by who on 28.09.2017.
 */

public interface ISignupView {

    void logOut();

    void updateAvatar(String image);

    void onError(String message);

    void takePhoto(Intent intent, int i);

    void updateAvatar(Bitmap bitmap);

    void saveData();

    void setStaregeEnabled();
}
