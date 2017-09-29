package com.example.who.doittest.presenter;

import android.content.Context;

import com.example.who.doittest.interfaces.IGalleryView;

/**
 * Created by who on 29.09.2017.
 */

public class GalleryActivityPresenter {

    Context context;
    IGalleryView view;

    public GalleryActivityPresenter(Context context, IGalleryView view) {
        this.context = context;
        this.view = view;
    }


}
