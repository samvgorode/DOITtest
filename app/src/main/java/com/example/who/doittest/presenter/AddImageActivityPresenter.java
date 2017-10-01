package com.example.who.doittest.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.who.doittest.controller.RestManager;
import com.example.who.doittest.interfaces.IAddImageView;
import com.example.who.doittest.utils.FileUtils;
import com.example.who.doittest.utils.PermissionUtils;
import com.orhanobut.hawk.Hawk;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.who.doittest.global.Constants.CHOOSE_OPEN_PHOTO;
import static com.example.who.doittest.global.Constants.TOKEN;

/**
 * Created by who on 29.09.2017.
 */

public class AddImageActivityPresenter {

    Context context;
    IAddImageView view;
    private RestManager manager;
    private Call<ResponseBody> postImageCall;


    public AddImageActivityPresenter(Context context, IAddImageView view) {
        this.context = context;
        this.view = view;
        manager = new RestManager();
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

    public void addNewImage(MultipartBody.Part image, RequestBody description, RequestBody hashtag, RequestBody latitude, RequestBody longitude) {
        String token = "";
        if (Hawk.contains(TOKEN)) token = Hawk.get(TOKEN);
        postImageCall = manager.getDoItService().postNewImage(token, image, description, hashtag, latitude, longitude);
        postImageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) view.onAddImageSuccess();
                else {
                    if (response.code() == 400) {
                        view.onAddImageFailure("Bad request");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onAddImageFailure(t.getLocalizedMessage());
            }
        });
    }
}
