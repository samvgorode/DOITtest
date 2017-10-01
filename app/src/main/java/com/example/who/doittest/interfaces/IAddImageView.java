package com.example.who.doittest.interfaces;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by who on 29.09.2017.
 */

public interface IAddImageView {

    void onAddImageSuccess();

    void onAddImageFailure(String what);

    void takePhoto(Intent intent, int i);

    void setStorageEnabled();

    void updatePlaceholder(Uri uri);
}
