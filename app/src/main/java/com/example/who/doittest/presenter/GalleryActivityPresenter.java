package com.example.who.doittest.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.who.doittest.controller.RestManager;
import com.example.who.doittest.interfaces.IGalleryView;
import com.example.who.doittest.pojo.ImagePojo;
import com.example.who.doittest.pojo.ListImages;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.who.doittest.global.Constants.TOKEN;

/**
 * Created by who on 29.09.2017.
 */

public class GalleryActivityPresenter {

    Context context;
    IGalleryView view;
    private Call<ListImages> allImagesCall;
    private RestManager restManager;

    public GalleryActivityPresenter(Context context, IGalleryView view) {
        this.context = context;
        this.view = view;
        restManager = new RestManager();
    }


    public void fetchImages() {
        String token = "";
        if (Hawk.contains(TOKEN)) token = Hawk.get(TOKEN);
        allImagesCall = restManager.getDoItService().getAllImages(token);
        allImagesCall.enqueue(new Callback<ListImages>() {
            @Override
            public void onResponse(Call<ListImages> call, Response<ListImages> response) {
                if (response.isSuccessful()) {
                    view.onFetchImagesSuccess();
                    if (response.body() != null) {
                        List<ImagePojo> listImages = response.body().getImages();
                        if (listImages.size() > 0)
                            view.setDataToAdapter(listImages);
                    }
                } else view.onFetchImagesFailure();
            }

            @Override
            public void onFailure(Call<ListImages> call, Throwable t) {
                view.onFetchImagesFailure();
            }
        });
    }
}
