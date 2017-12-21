package com.decobarri.decobarri.db_resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 19/12/2017.
 */

public class Image {

    @SerializedName("image")
    @Expose
    private String image;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
