package com.example.who.doittest.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * Created by who on 02.10.2017.
 */

public class GifUtils {

    private GifUtils() {}

    public static void showGif(Activity context, String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        WebView view = new WebView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        builder.setView(view);
        builder.create().show();
        view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        view.loadUrl(url);
    }
}
