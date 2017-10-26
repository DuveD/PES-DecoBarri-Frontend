package com.decobarri.decobarri.ActivityResources;

import android.graphics.Bitmap;

public class globalMaterialListItem {
    /*
    StructureMaterial {
        name: string
        description: string
        urgent: boolean
        quantity: integer
        address: string
    }
    */
    private Bitmap image;
    private String name;
    private String description;
    private boolean urgent;
    private int quantity;
    private String adress;

    public globalMaterialListItem(Bitmap image, String name, String description, boolean urgent, int quantity, String adress) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.urgent = urgent;
        this.quantity = quantity;
        this.adress = adress;
    }

    public Bitmap get_image() {
        return image;
    }

    public String get_name() {
        return name;
    }

    public String get_description() {
        return description;
    }

    public boolean is_urgent(){
        return urgent;
    }

    public int get_quantity() {
        return quantity;
    }

    public String get_adress() {
        return adress;
    }
}
