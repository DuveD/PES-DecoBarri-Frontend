package com.decobarri.decobarri.activity_resources.Items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Item implements Parcelable {

    private String _id;
    private String img;
    private String name;
    private String description;
    private String localization;
    private ArrayList<String> materials;

    public Item (String id, String img, String name, String description, String localization, ArrayList<String> materials){
        this._id = id;
        this.img = img;
        this.name = name;
        this.description = description;
        this.localization = localization;
        this.materials = materials;
    }

    public Item (){

    }

    protected Item(Parcel in) {
        _id = in.readString();
        img = in.readString();
        name = in.readString();
        description = in.readString();
        localization = in.readString();
        materials = in.createStringArrayList();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public void setId (String _id) {
        this._id = _id;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setImage (String img) {
        this.img = img;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getID() { return _id; }

    public String getImage() {
        return img;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(img);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(localization);
        parcel.writeStringList(materials);
    }
}
