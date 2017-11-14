package com.decobarri.decobarri.activity_resources;

import android.graphics.Bitmap;

public class Material {
    /*
    StructureMaterial {
        id: string
        name: string
        description: string
        urgent: boolean
        quantity: integer
        address: string
    }
    */
    private String id;
    private Bitmap image;
    private String name;
    private String description;
    private boolean urgent;
    private int quantity;
    private String address;

    public Material(String id, Bitmap image, String name, String description, boolean urgent, int quantity, String address) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.urgent = urgent;
        this.quantity = quantity;
        this.address = address;
    }

    public String getID() { return id; }

    public Bitmap getImage() {
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
