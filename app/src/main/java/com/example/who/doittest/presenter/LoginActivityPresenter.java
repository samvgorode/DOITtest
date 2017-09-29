package com.example.who.doittest.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.who.doittest.controller.RestManager;
import com.example.who.doittest.interfaces.ILoginView;
import com.example.who.doittest.interfaces.ISignupView;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by who on 29.09.2017.
 */

public class LoginActivityPresenter {

    private Context context;
    private ILoginView view;
    private RestManager restManager;
    private Call<ResponseBody> loginCall;

    public LoginActivityPresenter(Context context, ILoginView view) {
        this.context = context;
        this.view = view;
        restManager = new RestManager();
    }

    public void loginUser(RequestBody email, RequestBody password) {
        loginCall = restManager.getDoItService().loginUser(email, password);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    view.onLoginSuccess();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    view.onLoginFailed();
                    Toast.makeText(context, "Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.onLoginFailed();
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
