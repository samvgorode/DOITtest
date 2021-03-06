package com.example.who.doittest.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.who.doittest.controller.RestManager;
import com.example.who.doittest.interfaces.ISignupView;
import com.example.who.doittest.pojo.SignUpResponse;
import com.example.who.doittest.utils.FileUtils;
import com.example.who.doittest.utils.PermissionUtils;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.who.doittest.global.Constants.CHOOSE_OPEN_PHOTO;
import static com.example.who.doittest.global.Constants.TOKEN;

/**
 * Created by who on 28.09.2017.
 */

public class SignupActivityPresenter {

    private Context context;
    private ISignupView view;
    private boolean isUpdatedAvatar;
    private RestManager restManager;
    private Call<SignUpResponse> signUpCall;

    public SignupActivityPresenter(Context context, ISignupView view) {
        this.context = context;
        this.view = view;
        restManager = new RestManager();
    }

    public void getPhotoFromSD() {
        Intent intent = FileUtils.getImageIntent();
        if (PermissionUtils.isEnabledStorageAccess(context)) {
            view.takePhoto(intent, CHOOSE_OPEN_PHOTO);
        } else view.setStorageEnabled();
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

    public void registerUser(RequestBody username, RequestBody email, RequestBody password, MultipartBody.Part body) {
        signUpCall = restManager.getDoItService().createUser(username, email, password, body/*, file*/);
        signUpCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token  = response.body().getToken();
                    if (!TextUtils.isEmpty(token)) Hawk.put(TOKEN, token.trim());
                    view.onSignupSuccess();
                } else {
                    if (response.code() == 400) {
                        view.onSignupFailed("Bad request");
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                view.onSignupFailed(t.getLocalizedMessage());
            }
        });
    }
}
