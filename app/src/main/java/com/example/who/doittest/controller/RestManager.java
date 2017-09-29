package com.example.who.doittest.controller;

import com.example.who.doittest.callback.DoItService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by who on 29.09.2017.
 */

public class RestManager {

    private DoItService doItService;
    private static final String API_BASE_URL = "http://api.doitserver.in.ua/";

    public RestManager() {}

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
    OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);

    public DoItService getDoItService() {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor);
        if (doItService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            doItService = retrofit.create(DoItService.class);
        }

        return doItService;
    }
}
