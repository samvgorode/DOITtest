package com.example.who.doittest.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.example.who.doittest.controller.RestManager;
import com.example.who.doittest.interfaces.ISignupView;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.who.doittest.global.Constants.CHOOSE_OPEN_PHOTO;

/**
 * Created by who on 28.09.2017.
 */

public class SignupActivityPresenter {

    private Context context;
    private ISignupView view;
    private String strManufacturer = android.os.Build.MANUFACTURER;
    private boolean isUpdatedAvatar;
    private RestManager restManager;
    private Call<ResponseBody> userCall;

    public SignupActivityPresenter(Context context, ISignupView view) {
        this.context = context;
        this.view = view;
        restManager = new RestManager();
    }

    public void getPhotoFromSD() {
        Intent intent;
        if (strManufacturer.equals("samsung")) {
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "image/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            view.takePhoto(intent, CHOOSE_OPEN_PHOTO);
        } else view.setStaregeEnabled();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_OPEN_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                view.updateAvatar(imageUri);
                isUpdatedAvatar = true;
            }
        }
    }

    public void registerUser(RequestBody username, RequestBody email, RequestBody password, RequestBody avatar) {
        userCall = restManager.getDoItService().createUser(username, email, password, avatar/*, file*/);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    view.onSignupSuccess();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    view.onSignupFailed();
                    Toast.makeText(context, "Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onSignupFailed();
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
