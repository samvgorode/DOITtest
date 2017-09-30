package com.example.who.doittest.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListImages {

    @SerializedName("images")
    @Expose
    private List<ImagePojo> images = null;

    public List<ImagePojo> getImages() {
        return images;
    }

    public void setImages(List<ImagePojo> images) {
        this.images = images;
    }
}

