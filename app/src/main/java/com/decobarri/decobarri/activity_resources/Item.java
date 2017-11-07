package com.decobarri.decobarri.activity_resources;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Item {

    private Bitmap image;
    private String name;
    private String description;
    private String localization;
    private ArrayList<String> materials;

    public Item (Bitmap image, String name, String description, String localization, ArrayList<String> materials){
        this.image = image;
        this.name = name;
        this.description = description;
        this.localization = localization;
        this.materials = materials;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocalization() {
        return localization;
    }

    public ArrayList<String> getMaterials() {
        return materials;
    }
}
