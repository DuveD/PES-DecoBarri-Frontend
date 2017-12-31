package com.decobarri.decobarri.db_resources;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by Asus on 19/12/2017.
 */

public class Image {

    @SerializedName("image")
    @Expose
    private File image;

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
