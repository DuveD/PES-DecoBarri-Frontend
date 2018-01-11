package com.decobarri.decobarri.activity_resources.Materials;

import android.os.Parcel;
import android.os.Parcelable;

import com.decobarri.decobarri.activity_resources.Notes.Note;

import java.io.Serializable;

public class Material implements Parcelable {

    private String material_id;
    private String image;
    private String name;
    private String description;
    private boolean urgent;
    private int quantity;
    private String address;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(material_id);
        parcel.writeString(image);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString((urgent ? "true" : "false"));
        parcel.writeInt(quantity);
        parcel.writeString(address);
    }

    public Material(String material_id, String image, String name, String description, boolean urgent, int quantity, String address) {
        this.material_id = material_id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.urgent = urgent;
        this.quantity = quantity;
        this.address = address;
    }

    public Material (){
        this.material_id = "";
        this.image = "";
        this.name = "";
        this.description = "";
        this.urgent = false;
        this.quantity = -1;
        this.address = "";
    }

    protected Material(Parcel in) {
        this.material_id = in.readString();
        this.image = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.urgent = in.readString().equals("true");
        this.quantity = in.readInt();
        this.address = in.readString();
    }

    public static final Creator<Material> CREATOR = new Creator<Material>() {
        @Override
        public Material createFromParcel(Parcel in) {
            return new Material(in);
        }

        @Override
        public Material[] newArray(int size) {
            return new Material[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getID() { return material_id; }

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
