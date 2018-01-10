package com.decobarri.decobarri.activity_resources.Materials;

import android.os.Parcelable;

import java.io.Serializable;

public class Material implements Serializable {

    private String _id;
    private String image;
    private String name;
    private String description;
    private boolean urgent;
    private int quantity;
    private String address;

    public Material(String id, String image, String name, String description, boolean urgent, int quantity, String address) {
        this._id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.urgent = urgent;
        this.quantity = quantity;
        this.address = address;
    }

    public String getID() { return _id; }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUrgent(){
        return urgent;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAddress() {
        return address;
    }
}
