package com.example.who.doittest.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by who on 28.09.2017.
 */

public interface ISignupView {

    void logOut();

    void updateAvatar(Uri image);

    void takePhoto(Intent intent, int i);

    void setStaregeEnabled();

    void onSignupSuccess();

    void onSignupFailed();
}
