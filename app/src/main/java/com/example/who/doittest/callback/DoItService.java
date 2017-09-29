package com.example.who.doittest.callback;


import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by who on 29.09.2017.
 */

public interface DoItService {

    @Multipart
    @POST("/create")
    Call<ResponseBody> createUser(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("avatar") RequestBody avatar
//            @Part("avatar") RequestBody avatar,
//            @Part MultipartBody.Part file
    );

//    @GET("/orgs/{org}")
//    Call<Organization> getOrganization(@Header("Authorization") String token, @Header("Accept") String accept, @Path("org") String orgname);
//
//    @GET("users/{owner}/repos")
//    Call<List<Repository>> getRepositories(@Header("Authorization") String token, @Header("Accept") String accept, @Path("owner") String owner);
}
