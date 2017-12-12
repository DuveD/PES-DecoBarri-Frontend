package com.decobarri.decobarri.activity_resources.Items;

import java.util.ArrayList;

public class Item {

    private String id;
    private String image;
    private String name;
    private String description;
    private String localization;
    private ArrayList<String> materials;

    public Item (String id, String image, String name, String description, String localization, ArrayList<String> materials){
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.localization = localization;
        this.materials = materials;
    }

    public Item (){

    }

    public void setId (String id) {
        this.id = id;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getID() { return id; }

    public String getImage() {
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
