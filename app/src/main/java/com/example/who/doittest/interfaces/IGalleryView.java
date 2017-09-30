package com.example.who.doittest.interfaces;

import com.example.who.doittest.pojo.ImagePojo;

import java.util.List;

/**
 * Created by who on 29.09.2017.
 */

public interface IGalleryView {

    void setDataToAdapter(List<ImagePojo> albumList);

    void onFetchImagesSuccess();

    void onFetchImagesFailure();

    void showGif(String url);
}
