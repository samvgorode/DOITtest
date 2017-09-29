package com.example.who.doittest.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.example.who.doittest.interfaces.IAddImageView;
import com.example.who.doittest.utils.FileUtils;
import com.example.who.doittest.utils.PermissionUtils;

import static com.example.who.doittest.global.Constants.CHOOSE_OPEN_PHOTO;

/**
 * Created by who on 29.09.2017.
 */

public class AddImageActivityPresenter {

    Context context;
    IAddImageView view;
    private String strManufacturer = android.os.Build.MANUFACTURER;

    public AddImageActivityPresenter(Context context, IAddImageView view) {
        this.context = context;
        this.view = view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_OPEN_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                view.updatePlaceholder(imageUri);
            }
        }
    }

    public void getPhotoFromSD() {
        Intent intent = FileUtils.getImageIntent();
        if (PermissionUtils.isEnabledStorageAccess(context)) {
            view.takePhoto(intent, CHOOSE_OPEN_PHOTO);
        } else view.setStorageEnabled();
    }
}
