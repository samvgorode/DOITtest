package com.example.who.doittest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GifResponse {

    @SerializedName("gif")
    @Expose
    private String gif;

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

}
