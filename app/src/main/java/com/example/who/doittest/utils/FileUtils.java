package com.example.who.doittest.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.Comparator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by who on 29.09.2017.
 */

public class FileUtils {

    private FileUtils() {} //private constructor to enforce Singleton pattern

    public static MultipartBody.Part uploadImage(String filePath, String param) {

        MultipartBody.Part body = null;
        try {
            body = MultipartBody.Part.createFormData("", "", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //profileUpdateRequest.setWebsite(lblWebsite.getText().toString().trim());
        if ((!filePath.equals(""))) {
            File file = new File(filePath);
            RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData(param, file.getName(), photo);
        }
        return body;
    }
}
