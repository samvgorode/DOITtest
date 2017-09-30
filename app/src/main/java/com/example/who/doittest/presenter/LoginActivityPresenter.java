package com.example.who.doittest.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.who.doittest.controller.RestManager;
import com.example.who.doittest.interfaces.ILoginView;
import com.example.who.doittest.interfaces.ISignupView;
import com.example.who.doittest.pojo.LoginResponse;
import com.orhanobut.hawk.Hawk;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.who.doittest.global.Constants.TOKEN;

/**
 * Created by who on 29.09.2017.
 */

public class LoginActivityPresenter {

    private Context context;
    private ILoginView view;
    private RestManager restManager;
    private Call<LoginResponse> loginCall;

    public LoginActivityPresenter(Context context, ILoginView view) {
        this.context = context;
        this.view = view;
        restManager = new RestManager();
    }

    public void loginUser(RequestBody email, RequestBody password) {
        loginCall = restManager.getDoItService().loginUser(email, password);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body()!=null){
                        String token = response.body().getToken();
                    if(Hawk.contains(TOKEN)) {
                        Hawk.delete(TOKEN);
                    } Hawk.put(TOKEN, token);
                    view.onLoginSuccess();}
                } else {
                    view.onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                view.onLoginFailed();
            }
        });
    }
}
