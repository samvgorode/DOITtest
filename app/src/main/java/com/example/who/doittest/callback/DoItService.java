package com.example.who.doittest.callback;


import com.example.who.doittest.pojo.GifResponse;
import com.example.who.doittest.pojo.ListImages;
import com.example.who.doittest.pojo.LoginResponse;
import com.example.who.doittest.pojo.SignUpResponse;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.who.doittest.global.Constants.TOKEN;

/**
 * Created by who on 29.09.2017.
 */

public interface DoItService {

    @Multipart
    @POST("/create")
    Call<SignUpResponse> createUser(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("/login")
    Call<LoginResponse> loginUser(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("/image")
    Call<ResponseBody> postNewImage(
            @Header(TOKEN) String token,
            @Part MultipartBody.Part image,
            @Part("description") RequestBody description,
            @Part("hashtag") RequestBody hashtag,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude
    );

    @GET("/all")
    Call<ListImages> getAllImages(@Header(TOKEN) String token);

    @GET("/gif")
    Call<GifResponse> getGif(@Header(TOKEN) String token);
}
